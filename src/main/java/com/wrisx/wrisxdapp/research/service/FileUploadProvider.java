package com.wrisx.wrisxdapp.research.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class FileUploadProvider {
    public File uploadFile(MultipartFile file, String directory) throws IOException {
        String fileName = file.getOriginalFilename();
        String filepath = Paths.get(directory, fileName).toString();
        File researchFile = new File(filepath);
        FileOutputStream fos = new FileOutputStream(researchFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(file.getBytes());
        bos.close();

        return researchFile;
    }
}
