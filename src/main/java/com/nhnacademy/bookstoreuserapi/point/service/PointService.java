package com.nhnacademy.bookstoreuserapi.point.service;

import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PointService {

    ResponsePoint savePoint(String userId, PointCreateRequest pointcreaterequest);

    Page<ResponsePoint> findAll(String userId, Pageable pageable);
}
