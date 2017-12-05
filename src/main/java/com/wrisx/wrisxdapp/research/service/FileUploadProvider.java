package com.wrisx.wrisxdapp.research.service;

import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class FileUploadProvider {
    private final Logger logger = LoggerFactory.getLogger(FileUploadProvider.class);

    public File uploadFile(MultipartFile file, String directory) {
        try {
            String fileName = file.getOriginalFilename();
            String filepath = Paths.get(directory, fileName).toString();
            File researchFile = new File(filepath);
            FileOutputStream fos = new FileOutputStream(researchFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(file.getBytes());
            bos.close();

            return researchFile;
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            throw new InternalServerErrorException(ex);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new BadRequestException(ex);
        }
    }
}
