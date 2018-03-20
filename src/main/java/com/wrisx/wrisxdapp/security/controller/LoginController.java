package com.wrisx.wrisxdapp.security.controller;

import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.security.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value = "/login", method = GET)
    public ResponseEntity<Void> login(
            HttpServletRequest request,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("password") String password) {
        logger.debug(MessageFormat.format("Login {0}", emailAddress));
        System.out.println("emailAddress: " + emailAddress + ", password: " + password);

        User user = loginService.authenticate(emailAddress, password);
        request.getSession().setAttribute("emailAddress", user.getEmailAddress());

        return ResponseEntity.ok().build();
    }
}
