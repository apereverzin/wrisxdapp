package com.wrisx.wrisxdapp.user.service;

import com.wrisx.wrisxdapp.common.DigestProvider;
import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.common.RandomStringProvider;
import com.wrisx.wrisxdapp.data.response.UserData;
import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static final String MD5_ALGORITHM = "MD5";
    private static final int NONCE_LENGTH = 10;

    private final EntityProvider entityProvider;
    private final UserDao userDao;
    private final RandomStringProvider randomStringProvider;
    private final DigestProvider digestProvider;

    @Autowired
    public UserService(EntityProvider entityProvider, UserDao userDao,
                       RandomStringProvider randomStringProvider,
                       DigestProvider digestProvider) {
        this.entityProvider = entityProvider;
        this.userDao = userDao;
        this.randomStringProvider = randomStringProvider;
        this.digestProvider = digestProvider;
    }

    public UserData getUser(String userAddress) {
        User user = entityProvider.getUserByAddress(userAddress);

        return new UserData(user);
    }

    public void deleteUserIfPossible(String userAddress) {
        User user = entityProvider.getUserByAddress(userAddress);

        try {
            userDao.delete(user);
        } catch (Exception ex) {
            logger.debug(ex.getMessage());
        }
    }
}
