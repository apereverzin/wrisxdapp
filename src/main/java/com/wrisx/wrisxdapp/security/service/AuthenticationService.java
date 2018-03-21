package com.wrisx.wrisxdapp.security.service;

import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import com.wrisx.wrisxdapp.errorhandling.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {
    private final UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void storeUserInSession(HttpServletRequest request, String emailAddress) {
        request.getSession().setAttribute("emailAddress", emailAddress);
    }

    public String authenticateRequest(HttpServletRequest request) {
        String emailAddress = (String)request.getSession().getAttribute("emailAddress");

        if (emailAddress == null) {
            throw new UnauthorizedException("User cannot be authorized");
        }

        return emailAddress;
    }

    public void authenticateRequest(HttpServletRequest request, String address) {
        String emailAddress = authenticateRequest(request);

        User user = userDao.findByEmailAddress(emailAddress);
        if (!user.getEmailAddress().equalsIgnoreCase(emailAddress)) {
            throw new UnauthorizedException("User not authorized");
        }
    }
}
