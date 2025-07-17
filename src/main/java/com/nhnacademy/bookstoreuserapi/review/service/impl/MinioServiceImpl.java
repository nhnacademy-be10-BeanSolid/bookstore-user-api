package com.nhnacademy.bookstoreuserapi.review.service.impl;

import com.nhnacademy.bookstoreuserapi.review.service.MinioService;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioEndpoint;


    @Override
    public void deleteImage(String imageUrl) {
        try {
            String path = new URL(imageUrl).getPath();
            String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);

            String objectName = decodedPath.replaceFirst("/" + bucketName + "/", "");

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("MinIO에서 이미지 삭제 성공: {}", objectName);
        } catch (Exception e) {
            log.error("MinIO 이미지 삭제 실패: {}", imageUrl, e);
        }
    }

    @Override
    public List<String> getAllImageUrls() {
        List<String> imageUrls = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).recursive(true).build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                String imageUrl = String.format("%s/%s/%s", minioEndpoint, bucketName, item.objectName());
                imageUrls.add(imageUrl);
            }
        } catch (Exception e) {
            log.error("MinIO에서 이미지 목록 가져오기 실패", e);
        }
        return imageUrls;
    }
}
