package com.nhnacademy.bookstoreuserapi.review.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {
    private String url;
    private String accessKey;
    private String secretKey;
}
