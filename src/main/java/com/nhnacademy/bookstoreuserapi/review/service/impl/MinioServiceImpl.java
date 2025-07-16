package com.nhnacademy.bookstoreuserapi.review.service.impl;

import com.nhnacademy.bookstoreuserapi.review.service.MinioService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public void deleteImage(String imageUrl) {
        try {
            String path = new URL(imageUrl).getPath();
            String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
            // substring 대신 replaceFirst를 사용하여 더 안정적으로 객체 이름 추출
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
}
