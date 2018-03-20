package com.wrisx.wrisxdapp.security.service;

import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import com.wrisx.wrisxdapp.errorhandling.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class LoginService {
    private final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final UserDao userDao;

    @Autowired
    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User authenticate(String emailAddress, String password) {
        User user = userDao.findByEmailAddressAndPassword(emailAddress, password);

        if (user == null) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", emailAddress));
        }

        return user;
    }
}
