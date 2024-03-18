package com.perfect.electronic.store.services.impl;

import com.perfect.electronic.store.exceptions.BadApiRequestException;
import com.perfect.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {
    private Logger logger= LoggerFactory.getLogger(FileService.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFileName=file.getOriginalFilename();
        logger.info("fileName: {}",originalFileName);
        String filename= UUID.randomUUID().toString();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameWithExtension=filename+extension;
        String fullPathWithFileName=path + fileNameWithExtension;
        logger.info("Full image Path :{}",fullPathWithFileName);
        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase("jpeg"))
        {
            //save file
            logger.info("file Extension {}",extension);
            File folder=new File(path);
            if(!folder.exists()){
                //create the folder
                logger.info("DDDDD");
                folder.mkdirs();
            }
            //upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }else{
            throw new BadApiRequestException("File With"+extension+"Not Allowed");
        }

    }
    @Override
    public InputStream getResources(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
