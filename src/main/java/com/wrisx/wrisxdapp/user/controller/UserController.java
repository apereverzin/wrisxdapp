package com.wrisx.wrisxdapp.user.controller;

import com.wrisx.wrisxdapp.data.response.UserData;
import com.wrisx.wrisxdapp.security.service.AuthenticationService;
import com.wrisx.wrisxdapp.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/user/{address}", method = GET)
    public ResponseEntity<UserData> getUser(
            @PathVariable("address") String userAddress,
            HttpServletRequest request) {
        logger.debug(MessageFormat.format("Getting user {0}", userAddress));

        UserData user = userService.getUser(userAddress);

        return ResponseEntity.ok(user);
    }
}
