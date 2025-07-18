package com.nhnacademy.bookstoreuserapi.user.service.impl;

import com.nhnacademy.bookstoreuserapi.adapter.OrderAdapter;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.user.domain.*;
import com.nhnacademy.bookstoreuserapi.user.event.UserRegisteredEvent;
import com.nhnacademy.bookstoreuserapi.user.exception.PointNotEnoughException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGradeRepository userGradeRepository;
    private final PointService pointService;
    private final OrderAdapter orderAdapter;
    private final EntityManager entityManager;
    private final PointTypeService pointTypeService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResponseUser getUser(String userId) {
        User user = Optional.ofNullable(userRepository.findByUserId(userId))
                .orElseThrow(() -> new UserNotFoundException(userId));
        return new ResponseUser(user);
    }

    @Override
    public ResponseUser getUserByUserNo(Long userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new UserNotFoundException(userNo));
        return new ResponseUser(user);
    }

    @Override
    public ResponseUserId getUserIdByUserNameAndUserEmail(String userName, String userEmail){
        User user = userRepository.findByUserNameAndUserEmail(userName, userEmail);
        if(user == null){
            throw new UserNotFoundException(userName, userEmail);
        }
        return new ResponseUserId(user);
    }

    @Override
    public ResponseUser saveUser(UserCreateRequest request) {

        if(userRepository.existsByUserId(request.userId())){
            throw new UserAlreadyExistException(request.userId());
        }

        String encodedPassword = passwordEncoder.encode(request.userPassword());
        UserGrade basicGrade = userGradeRepository.findByGradeName(BASIC);

        User user = new User(
                request.userId(),
                encodedPassword,
                request.userName(),
                request.userPhoneNumber(),
                request.userEmail(),
                request.userBirth()
        );

        user.setAuth(false);
        user.setUserPoint(0);

        user.setCreatedAt(LocalDateTime.now());
        user.setUserGrade(basicGrade);
        user.setUserStatus(User.Status.ACTIVE);

        User savedUser = userRepository.save(user);

        // 유형별 적립테이블의 회원가입 값에 따라 포인트 적립 액수가 달라짐
        if(pointTypeService.isActivePointType("회원가입")){

            int welcomePoint = pointTypeService.getEarningPointByTypeName("회원가입");
            Long typeId = pointTypeService.getTypeIdByName("회원가입");

            user.setUserPoint(welcomePoint);

            String welcomePointPlus = welcomePoint + "p 적립";

            PointCreateRequest pointCreateRequest = new PointCreateRequest(
                    request.userId(),
                    typeId,
                    null,
                    LocalDateTime.now(),
                    welcomePointPlus
            );

            pointService.savePoint(request.userId(),pointCreateRequest);
        }

        // RabbitMQ로 웰컴 쿠폰 발급 이벤트 발행
        rabbitTemplate.convertAndSend("user-exchange", "user.registered", new UserRegisteredEvent(savedUser.getUserNo()));
        log.info("User registered event published for userNo={}", savedUser.getUserNo());

        return new ResponseUser(savedUser);
    }

    @Override
    public ResponseUser saveOauth2User(Oauth2UserCreateRequest request) {

        String userId = request.provider() + request.providerId();

        if(userRepository.existsByUserId(userId)){
            throw new UserAlreadyExistException(userId);
        }

        UserGrade basicGrade = userGradeRepository.findByGradeName(BASIC);

        User user = new User(
                request.provider(),
                request.providerId()
        );
        user.setUserName(request.userName());
        user.setUserPhoneNumber(request.userPhoneNumber());
        user.setUserEmail(request.userEmail());
        user.setUserBirth(request.userBirth());
        user.setUserPoint(0);

        user.setAuth(false);

        user.setCreatedAt(LocalDateTime.now());
        user.setUserGrade(basicGrade);
        user.setUserStatus(User.Status.ACTIVE);

        User savedUser = userRepository.save(user);

        // 유형별 적립테이블의 회원가입 값에 따라 포인트 적립 액수가 달라짐
        if(pointTypeService.isActivePointType("회원가입")){

            int welcomePoint = pointTypeService.getEarningPointByTypeName("회원가입");
            Long typeId = pointTypeService.getTypeIdByName("회원가입");

            user.setUserPoint(welcomePoint);

            String welcomePointPlus = welcomePoint + "p 적립";

            PointCreateRequest pointCreateRequest = new PointCreateRequest(
                    userId,
                    typeId,
                    null,
                    LocalDateTime.now(),
                    welcomePointPlus
            );

            pointService.savePoint(userId,pointCreateRequest);
        }

        // RabbitMQ로 웰컴 쿠폰 발급 이벤트 발행
        rabbitTemplate.convertAndSend("user-exchange", "user.registered", new UserRegisteredEvent(savedUser.getUserNo()));
        log.info("User registered event published for userNo={}", savedUser.getUserNo());

        return new ResponseUser(savedUser);
    }

    @Override
    public ResponseUser updatePersonalInformation(String userId, UserUpdateRequest request) {

        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }

        User user = userRepository.findByUserId(userId);

        if(request.userPassword()!=null){
            String encodedPassword = passwordEncoder.encode(request.userPassword());
            user.setUserPassword(encodedPassword);
        }
        user.setUserName(request.userName());
        user.setUserPhoneNumber(request.userPhoneNumber());
        user.setUserEmail(request.userEmail());
        user.setUserBirth(request.userBirth());

        return new ResponseUser(user);
    }

    @Override
    public ResponseUser updateLastLoginAt(String userId) {

        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updateLastLoginByUserId(userId, LocalDateTime.now());

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    // 포인트 적립
    @Override
    public ResponseUser plusPoint(Long userNo, int point) {

        String userId = userRepository.findUserIdByUserNo(userNo);

        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updatePointByUserId(userId, point);

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    // 포인트 차감
    @Override
    public ResponseUser minusPoint(Long userNo, int point) {

        String userId = userRepository.findUserIdByUserNo(userNo);

        if(!isUserExist(userId)){
            throw new UserNotFoundException(userId);
        }

        if(getUserPoint(userId) < point){
            throw new PointNotEnoughException(userId, point);
        }

        userRepository.updatePointByUserId(userId, -point);

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    public ResponseUser updateUserStatus(String userId, String status) {

        User.Status status1 = User.Status.valueOf(status);

        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updateStatusByUserId(userId, status1);

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    public void bulkUpdateUserGrades() {
        List<UserGrade> userGrades = userGradeRepository.findAll();
        userGrades.sort(Comparator.comparing(UserGrade::getRequiredMoney).reversed());

        Map<Long, Long> userNoToPureOrderAmount =
                Optional.ofNullable(orderAdapter.getOrderAmountGroupByUserLastThreeMonth().getBody())
                        .orElse(Collections.emptyList())
                        .stream()
                        .collect(Collectors.toMap(UserOrderAmountResponse::userNo, UserOrderAmountResponse::getpureOrderAmount));

        for (UserGrade grade : userGrades) {
            List<Long> userNosToCheck = userNoToPureOrderAmount.entrySet().stream()
                    .filter(entry -> entry.getValue() >= grade.getRequiredMoney())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (!userNosToCheck.isEmpty()) {
                // 등급이 다른 사용자만 필터링
                List<Long> userNosToUpdate = userRepository.findUserNosWithDifferentGrade(userNosToCheck, grade.getGradeName());

                if (!userNosToUpdate.isEmpty()) {
                    long updated = userRepository.bulkUpdateUserGrade(grade, userNosToUpdate);
                    log.debug(updated + " users updated to " + grade.getGradeName());
                    userNosToUpdate.forEach(userNoToPureOrderAmount::remove);
                } else {
                    userNosToCheck.forEach(userNoToPureOrderAmount::remove);
                }
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public ResponseUser deleteUser(String userId) {
        return updateUserStatus(userId, "WITHDRAWN");
    }

    @Override
    public boolean isUserExist(String userId) {

        return userRepository.existsByUserId(userId);
    }

    @Override
    public int getUserPoint(String userId) {

        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }

        return userRepository.findUserPointByUserId(userId);
    }

    @Override
    public int getUserPointByUserNo(Long userNo) {
        ResponseUser user = getUserByUserNo(userNo);
        return getUserPoint(user.getUserId());
    }

    @Override
    public UserGrade.Grade getUserGradeByUserNo(Long userNo) {
        ResponseUser user = getUserByUserNo(userNo);
        return UserGrade.Grade.valueOf(user.getUserGradeName());
    }
}
