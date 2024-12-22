package com.hust.smart_Shopping.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile file, String uploadFolder);
}
