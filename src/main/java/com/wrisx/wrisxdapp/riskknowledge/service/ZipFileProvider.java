package com.wrisx.wrisxdapp.riskknowledge.service;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import static com.wrisx.wrisxdapp.riskknowledge.service.RiskKnowledgeService.ZIP_EXTENSION;

@Component
public class ZipFileProvider {

    public ZipFile zipAndProtectFile(File file, String directory, String password) throws Exception {
        String zipFileName = UUID.randomUUID().toString();

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        zipParameters.setEncryptFiles(true);
        zipParameters.setPassword(password);
        String destinationZipFilePath = Paths.get(directory, zipFileName + ZIP_EXTENSION).toString();
        ZipFile zipFile = new ZipFile(destinationZipFilePath);
        zipFile.createZipFile(file, zipParameters);

        return zipFile;
    }
}
