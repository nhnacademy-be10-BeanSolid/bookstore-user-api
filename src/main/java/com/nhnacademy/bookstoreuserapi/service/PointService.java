package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;

import java.util.List;

public interface PointService {

    ResponsePoint savePoint(PointCreateRequest pointcreaterequest);

    List<ResponsePoint> findAll(String userId);
}
