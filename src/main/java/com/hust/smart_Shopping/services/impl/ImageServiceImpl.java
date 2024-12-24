package com.hust.smart_Shopping.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hust.smart_Shopping.exceptions.payload.UploadImageException;
import com.hust.smart_Shopping.services.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file, String uploadFolder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", uploadFolder));
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new UploadImageException("[cloudinary] can't upload file");
        }
    }

}
