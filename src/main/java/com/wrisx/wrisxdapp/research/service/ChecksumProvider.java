package com.wrisx.wrisxdapp.research.service;

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
public class ChecksumProvider {
    private final Logger logger = LoggerFactory.getLogger(ChecksumProvider.class);

    public String getFileChecksum(File file, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];

            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            byte[] mdbytes = md.digest();

            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
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
}
