package com.nhnacademy.bookstoreuserapi.user.service.impl;

import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.user.domain.Oauth2UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUserId;
import com.nhnacademy.bookstoreuserapi.usergrade.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.exception.UserAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGradeRepository userGradeRepository;
    private final PointService pointService;
    private final PointTypeService pointTypeService;

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

        // 유형별 적립테이블의 회원가입 값에 따라 포인트 적립 액수가 달라짐

        pointTypeService.isActivePointType("회원가입");

        int welcomePoint = pointTypeService.getEarningPointByTypeName("회원가입");
        Long typeId = pointTypeService.getTypeIdByName("회원가입");

        user.setUserPoint(welcomePoint);

        String welcomePointPlus = welcomePoint + " 적립";

        user.setCreatedAt(LocalDateTime.now());
        user.setUserGrade(basicGrade);
        user.setUserStatus(User.Status.ACTIVE);

        User savedUser = userRepository.save(user);

        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                request.userId(),
                typeId,
                null,
                LocalDateTime.now(),
                welcomePointPlus
        );

        pointService.savePoint(request.userId(),pointCreateRequest);

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

        user.setAuth(false);

        // 유형별 적립테이블의 회원가입 값에 따라 포인트 적립 액수가 달라짐



        pointTypeService.isActivePointType("회원가입");

        int welcomePoint = pointTypeService.getEarningPointByTypeName("회원가입");
        Long typeId = pointTypeService.getTypeIdByName("회원가입");

        user.setUserPoint(welcomePoint);

        String welcomePointPlus = welcomePoint + " 적립";

        user.setUserGrade(basicGrade);
        user.setUserStatus(User.Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                userId,
                typeId,
                null,
                LocalDateTime.now(),
                welcomePointPlus
        );

        pointService.savePoint(userId,pointCreateRequest);

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

    @Override
    public ResponseUser updatePoint(String userId, int point) {

        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updatePointByUserId(userId, point);

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    public ResponseUser updateUserStatus(String userId, User.Status status) {

        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updateStatusByUserId(userId, status);

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    public ResponseUser updateUserGradeName(String userId, String gradeName) {
        if(!userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(userId);
        }
        UserGrade.Grade grade;
        try {
            grade = UserGrade.Grade.valueOf(gradeName);
        } catch (IllegalArgumentException e) {
            throw new UserGradeNotFoundException(gradeName);
        }

        UserGrade userGrade = userGradeRepository.findByGradeName(grade);
        if (userGrade == null) {
            throw new UserGradeNotFoundException(gradeName);
        }
        userRepository.updateUserGrade_gradeNameByUserId(userId, userGrade.getGradeName());

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    public ResponseUser deleteUser(String userId) {
        return updateUserStatus(userId, User.Status.WITHDRAWN);
    }

    @Override
    public boolean isUserExist(String userId) {

        return userRepository.existsByUserId(userId);
    }
}
