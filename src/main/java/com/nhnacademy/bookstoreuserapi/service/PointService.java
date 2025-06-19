package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PointService {

    ResponsePoint savePoint(String userId, PointCreateRequest pointcreaterequest);

    Page<ResponsePoint> findAll(String userId, Pageable pageable);
}
