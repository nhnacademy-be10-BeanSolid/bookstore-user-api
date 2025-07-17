package com.nhnacademy.bookstoreuserapi.review.service;

import java.util.List;

public interface MinioService {
    void deleteImage(String imageUrl);
    List<String> getAllImageUrls();
}
