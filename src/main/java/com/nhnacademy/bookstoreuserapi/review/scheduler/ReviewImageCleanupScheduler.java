package com.nhnacademy.bookstoreuserapi.review.scheduler;

import com.nhnacademy.bookstoreuserapi.review.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreuserapi.review.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewImageCleanupScheduler {

    private final ReviewImageRepository reviewImageRepository;
    private final MinioService minioService;

    // DB에 저장된 리뷰 이미지와 MinIO에 저장된 이미지의 불일치 문제를 해결하기 위한 스케줄러
    // 매주 월요일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 ? * MON")
    public void cleanupOrphanedReviewImages() {
        log.info("리뷰 이미지 정리 스케줄러 시작...");

        // 1. DB에 저장된 모든 리뷰 이미지 URL 가져오기
        Set<String> dbImageUrls = reviewImageRepository.findAll().stream()
                .map(reviewImage -> {
                    String imageUrl = reviewImage.getImageUrl();
                    int queryParamIndex = imageUrl.indexOf('?');
                    if (queryParamIndex != -1) {
                        imageUrl = imageUrl.substring(0, queryParamIndex);
                    }
                    return imageUrl;
                })
                .collect(Collectors.toSet());

        // 2. MinIO에 저장된 모든 이미지 URL 가져오기
        List<String> minioImageUrls = minioService.getAllImageUrls();

        // 3. MinIO에는 있지만 DB에는 없는 이미지 식별 (이미지)
        List<String> orphanedMinioImages = minioImageUrls.stream()
                .filter(minioUrl -> !dbImageUrls.contains(minioUrl))
                .toList();

        // 4. 이미지 삭제
        if (orphanedMinioImages.isEmpty()) {
            log.info("삭제할 이미지가 없습니다.");
        } else {
            log.info("{}개의 이미지를 MinIO에서 삭제합니다.", orphanedMinioImages.size());
            for (String imageUrl : orphanedMinioImages) {
                minioService.deleteImage(imageUrl);
            }
        }
        log.info("리뷰 이미지 정리 스케줄러 종료.");
    }
}