package com.nhnacademy.bookstoreuserapi.review.service.impl;


import com.nhnacademy.bookstoreuserapi.adapter.BookAdapter;
import com.nhnacademy.bookstoreuserapi.adapter.OrderAdapter;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.review.domain.*;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewNotAllowedException;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.review.service.MinioService;
import com.nhnacademy.bookstoreuserapi.review.service.ReviewService;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nhnacademy.bookstoreuserapi.common.exception.OwnerShipValidator.validate;
import static java.time.LocalDateTime.now;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PointService pointService;
    private final PointTypeService pointTypeService;
    private final MinioService minioService;
    private final OrderAdapter orderAdapter;
    private final BookAdapter bookAdapter;



    @Override
    public ResponseReview addReview(String userId, ReviewCreateRequest review) {
        User user = userRepository.findByUserId(review.userId());

        if (user == null) {
            throw new UserNotFoundException(review.userId());
        }

        Long userNo = user.getUserNo();

        // 주문 내역 확인
        if (!orderAdapter.validatePurchase(userNo, review.bookId())){
            throw new ReviewNotAllowedException("해당 사용자는 이 책을 구매하지 않았습니다.");
        }

        // 중복 리뷰 확인
        Review findReview = reviewRepository.findByUser_UserIdAndBookId(review.userId(), review.bookId());
        if (findReview != null) {
            throw new ReviewAlreadyExistsBookException(review.userId(), review.bookId());
        }

        validate(userId, review.userId());



        // 리뷰 저장
        Review savedReview = reviewRepository.save(new Review(review, user));

        // 포인트 처리
        if (pointTypeService.isActivePointType("리뷰") || pointTypeService.isActivePointType("리뷰+사진")) {
            String pointTypeName = (review.imageUrls() == null || review.imageUrls().isEmpty()) ? "리뷰" : "리뷰+사진";
            int reviewPoint = pointTypeService.getEarningPointByTypeName(pointTypeName);
            long typeId = pointTypeService.getTypeIdByName(pointTypeName);

            userRepository.updatePointByUserId(review.userId(), reviewPoint);

            PointCreateRequest pointCreateRequest = new PointCreateRequest(
                    userId,
                    typeId,
                    null,
                    now(),
                    reviewPoint + "p 적립"
            );

            pointService.savePoint(userId, pointCreateRequest);
        }

        updateBookDocument(review.bookId());

        return ResponseReview.from(savedReview);
    }

    @Override
    public ResponseReview editReview(String userId, long reviewId, ReviewUpdateRequest review) {
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        validate(userId, findReview.getUser().getUserId());

        boolean hadImageInitially = !findReview.getReviewImages().isEmpty();

        // 이미지 처리
        updateReviewImages(findReview, review.imageUrls());

        // 기타 정보 수정
        findReview.setEvaluationScore(review.evaluationScore());
        findReview.setReviewContent(review.reviewContent());
        findReview.setUpdatedAt(now());

        boolean hasImageNow = !findReview.getReviewImages().isEmpty();

        // 포인트 정책 반영
        if (!hadImageInitially && hasImageNow && pointTypeService.isActivePointType("리뷰사진추가")) {
            handlePoint(userId, "리뷰사진추가");
        } else if (hadImageInitially && !hasImageNow && pointTypeService.isActivePointType("리뷰사진삭제")) {
            handlePoint(userId, "리뷰사진삭제");
        }
        reviewRepository.save(findReview);

        updateBookDocument(findReview.getBookId());

        return ResponseReview.from(findReview);
    }

    private void updateReviewImages(Review review, List<String> newImageUrls) {
        Set<String> newImageUrlSet = new HashSet<>(newImageUrls);
        List<ReviewImage> imagesToKeep = new ArrayList<>();
        List<ReviewImage> imagesToRemove = new ArrayList<>();

        // 기존 이미지를 보관할 것과 삭제할 것으로 분류
        for (ReviewImage existingImage : review.getReviewImages()) {
            if (newImageUrlSet.contains(existingImage.getImageUrl())) {
                imagesToKeep.add(existingImage);
            } else {
                imagesToRemove.add(existingImage);
            }
        }

        // 삭제할 이미지 처리 (MinIO)
        for (ReviewImage image : imagesToRemove) {
            minioService.deleteImage(image.getImageUrl());
        }

        // 추가할 새 이미지 URL 찾기
        Set<String> keptImageUrlSet = new HashSet<>();
        for (ReviewImage img : imagesToKeep) {
            keptImageUrlSet.add(img.getImageUrl());
        }

        for (String newUrl : newImageUrls) {
            if (!keptImageUrlSet.contains(newUrl)) {
                ReviewImage newImage = new ReviewImage();
                newImage.setImageUrl(newUrl);
                newImage.setUploadedAt(now());
                newImage.setReview(review);
                imagesToKeep.add(newImage);
            }
        }

        // 엔티티의 컬렉션을 완전히 새로운 리스트로 교체하여 JPA 변경 감지를 확실히 유발
        review.getReviewImages().clear();
        review.getReviewImages().addAll(imagesToKeep);
    }

    private void handlePoint(String userId, String typeName) {
        int rawPoint = pointTypeService.getEarningPointByTypeName(typeName);
        int appliedPoint = typeName.equals("리뷰사진삭제") ? -rawPoint : rawPoint;
        long typeId = pointTypeService.getTypeIdByName(typeName);

        userRepository.updatePointByUserId(userId, appliedPoint);

        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                userId,
                typeId,
                null,
                now(),
                rawPoint + "p " + (appliedPoint > 0 ? "적립" : "차감")
        );

        pointService.savePoint(userId, pointCreateRequest);
    }

    @Override
    public ResponseReview getReview(long reviewId) {
        Review findReview = reviewRepository.findById(reviewId).orElse(null);
        if (findReview == null) {
            throw new ReviewNotFoundException(reviewId);
        }
        return ResponseReview.from(findReview);
    }

    @Override
    public Page<ResponseSimpleReviewByUser> getReviewsByUserId(String userId, Pageable pageable) {
        return reviewRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<ResponseSimpleReview> getReviewsByBookId(long bookId, Pageable pageable) {
        return reviewRepository.findAllByBookId(bookId, pageable);
    }

    @Override
    public long countReviewsByBookId(long bookId) {
        return reviewRepository.countReviewsByBookId((bookId));
    }

    @Override
    public double getAverageEvaluationScoreByBookId(long bookId) {
        return reviewRepository.averageEvaluationScoreByBookId(bookId);
    }

    public void updateBookDocument(long bookId){
        Double averageScore = getAverageEvaluationScoreByBookId(bookId);
        Long reviewCount = countReviewsByBookId(bookId);

        bookAdapter.updateBookDocument(bookId, reviewCount, averageScore);
    }

}
