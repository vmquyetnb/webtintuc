package com.technews.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER = "D:\\STUDY\\Learning\\Back-end\\Back-demo\\tech-main\\technews\\Admin\\src\\main\\resources\\static\\img\\image-post";

    public boolean uploadImage(MultipartFile imagePost){
        boolean isUpload = false;
        try {
            Files.copy(imagePost.getInputStream(),
                    Paths.get(UPLOAD_FOLDER + File.separator, imagePost.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return isUpload;
    }

    public boolean checkExisted(MultipartFile imagePost){
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + imagePost.getOriginalFilename());
            isExisted = file.exists();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isExisted;
    }
}
