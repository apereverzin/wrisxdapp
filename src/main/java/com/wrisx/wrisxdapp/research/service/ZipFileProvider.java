package com.wrisx.wrisxdapp.research.service;

import com.wrisx.wrisxdapp.errorhandling.InternalServerErrorException;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import static com.wrisx.wrisxdapp.research.service.ResearchService.ZIP_EXTENSION;

@Component
public class ZipFileProvider {
    private final Logger logger = LoggerFactory.getLogger(ZipFileProvider.class);

    public ZipFile zipAndProtectFile(File file, String directory, String password) {
        try {
            String zipFileName = UUID.randomUUID().toString();

            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipParameters.setEncryptFiles(true);
            zipParameters.setPassword(password);
            String destinationZipFilePath =
                    Paths.get(directory, zipFileName + ZIP_EXTENSION).toString();
            ZipFile zipFile = new ZipFile(destinationZipFilePath);
            zipFile.createZipFile(file, zipParameters);

            return zipFile;
        } catch (ZipException ex) {
            logger.error(ex.getMessage(), ex);
            throw new InternalServerErrorException(ex);
        }
    }

    public ZipFile zipAndProtectUploadedFile(MultipartFile file, String directory, String password) {
        try {
            String zipFileName = UUID.randomUUID().toString();

            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipParameters.setEncryptFiles(true);
            zipParameters.setSourceExternalStream(true);
            zipParameters.setFileNameInZip(file.getOriginalFilename());
            zipParameters.setPassword(password);
            String destinationZipFilePath =
                    Paths.get(directory, zipFileName + ZIP_EXTENSION).toString();
            ZipFile zipFile = new ZipFile(destinationZipFilePath);
            zipFile.addStream(file.getInputStream(), zipParameters);

            return zipFile;
        } catch (ZipException | IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new InternalServerErrorException(ex);
        }
    }
}
