package com.wrisx.wrisxdapp.user.controller;

import com.wrisx.wrisxdapp.data.response.UserData;
import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import com.wrisx.wrisxdapp.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserDao userDao;

    public static final String USER_ADDRESS = "User-Address";
    public static final String USER_AUTHORISED = "User-Authorised";
    private static final String USER_NONCE = "User-Nonce";

    @Autowired
    public UserController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/nonce", method = GET)
    public ResponseEntity<String> getNonce(HttpServletRequest request,
                                           @RequestParam(name = "address") String userAddress) {
        logger.debug("Getting nonce");

        request.getSession().setAttribute(USER_ADDRESS, userAddress);

        User user = userDao.findByAddress(userAddress);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }

        String nonce = userService.getNonce();
        request.getSession().setAttribute(USER_NONCE, nonce);

        return ResponseEntity.ok(nonce);
    }

    @RequestMapping(value = "/authorise", method = POST)
    public ResponseEntity<Void> verifyHash(
            HttpServletRequest request,
            @SessionAttribute(USER_ADDRESS) String userAddress,
            @SessionAttribute(USER_NONCE) String nonce,
            @RequestParam("hash") String hash) {
        logger.debug(MessageFormat.format("Getting secret {0}", userAddress));

        userService.verifyHash(userAddress, nonce, hash);
        request.getSession().setAttribute(USER_AUTHORISED, "true");

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user/{address}", method = GET)
    public ResponseEntity<UserData> getUser(
            @PathVariable("address") String userAddress) {
        logger.debug(MessageFormat.format("Getting user {0}", userAddress));

        UserData user = userService.getUser(userAddress);

        return ResponseEntity.ok(user);
    }
}
