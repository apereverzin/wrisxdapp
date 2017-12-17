package com.wrisx.wrisxdapp.init.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.text.MessageFormat;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class InitController {
    private final Logger logger = LoggerFactory.getLogger(InitController.class);

    public static final String USER_ADDRESS = "User-Address";

    @RequestMapping(value = "/init", method = GET)
    public ResponseEntity<Void> init(
            @RequestHeader(value="User-Address") String userAddress,
            HttpServletRequest request) {
        logger.debug(MessageFormat.format("Initializing {0}", userAddress));

        request.getSession().setAttribute(USER_ADDRESS, userAddress);

        return ResponseEntity.ok().build();
    }
}
