package com.wrisx.wrisxdapp.user.controller;

import com.wrisx.wrisxdapp.data.UserData;
import com.wrisx.wrisxdapp.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static com.wrisx.wrisxdapp.init.controller.InitController.USER_ADDRESS;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user", method = GET)
    public ResponseEntity<UserData> getUser(HttpServletRequest request) {
        String userAddress = (String)request.getSession().getAttribute(USER_ADDRESS);
        logger.debug(MessageFormat.format("Getting user {0}", userAddress));

        UserData user = userService.getUser(userAddress);

        if (user == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return new ResponseEntity<>(user, OK);
    }
}
