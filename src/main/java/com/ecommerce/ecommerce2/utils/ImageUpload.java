package com.ecommerce.ecommerce2.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER = "C:/Users/ty/Documents/nouveau_espace/ecommerce2/src/main/resources/static/imagerie";

    public boolean  uploadImage(MultipartFile imageProduct){
        boolean isUpload = false;
        try{
            Files.copy(imageProduct.getInputStream(), Paths.get(UPLOAD_FOLDER + File.separator, imageProduct.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            isUpload=true;

        }catch (Exception e){
            e.printStackTrace();
        }

        return isUpload;
    }

    public boolean checkExisted(MultipartFile imageProduit){
        boolean isExisted = false;

        try {
            File file=new File(UPLOAD_FOLDER +"/"+ imageProduit.getOriginalFilename());
            isExisted = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExisted;
    }
}
