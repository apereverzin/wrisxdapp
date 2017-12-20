package com.wrisx.wrisxdapp.user.service;

import com.wrisx.wrisxdapp.common.DigestProvider;
import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.common.RandomStringProvider;
import com.wrisx.wrisxdapp.data.UserData;
import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.errorhandling.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static final String MD5_ALGORITHM = "MD5";
    private static final int NONCE_LENGTH = 10;

    private final EntityProvider entityProvider;
    private final RandomStringProvider randomStringProvider;
    private final DigestProvider digestProvider;

    @Autowired
    public UserService(EntityProvider entityProvider,
                       RandomStringProvider randomStringProvider,
                       DigestProvider digestProvider) {
        this.entityProvider = entityProvider;
        this.randomStringProvider = randomStringProvider;
        this.digestProvider = digestProvider;
    }

    public UserData getUser(String userAddress) {
        User user = entityProvider.getUserByAddress(userAddress);

        return new UserData(user);
    }

    public String getNonce() {
        return randomStringProvider.getRandomString(NONCE_LENGTH);
    }

    public void verifyHash(String userAddress, String nonce, String hash) {
        User user = entityProvider.getUserByAddress(userAddress);

        String str = user.getSecret() + nonce;
        String res = digestProvider.getStringDigest(str, MD5_ALGORITHM);
        if (!res.equals(hash)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Cannot authorize {0}", userAddress));
        }
    }
}
