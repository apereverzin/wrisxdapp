package com.wrisx.wrisxdapp.common;

import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class DigestProvider {
    private final Logger logger = LoggerFactory.getLogger(DigestProvider.class);

    public String getFileDigest(File file, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];

            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            byte[] digestBytes = md.digest();

            return getDigestString(digestBytes);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage(), ex);
            throw new InternalServerErrorException(ex);
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            throw new InternalServerErrorException(ex);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new BadRequestException(ex);
        }
    }

    public String getStringDigest(String str, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            byte[] digestBytes = md.digest(str.getBytes());
            return getDigestString(digestBytes);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage(), ex);
            throw new InternalServerErrorException(ex);
        }
    }

    private String getDigestString(byte[] digestBytes) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < digestBytes.length; i++) {
            sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
