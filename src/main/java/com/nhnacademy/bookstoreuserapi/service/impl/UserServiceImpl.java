package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.service.UserService;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.exception.UserAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGradeRepository userGradeRepository;
    private final PointTypeRepository pointTypeRepository;

    @Override
    public ResponseUser getUser(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        User user = userRepository.findByUserId(userId);

        return new ResponseUser(user);
    }

    @Override
    @Transactional
    public ResponseUser saveUser(UserCreateRequest request) {

        if(userRepository.existsById(request.userId())){
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
        user.setUserPoint(pointTypeRepository.findEarningPointByTypeName("회원가입"));

        user.setUserGrade(basicGrade);
        user.setUserStatus(User.Status.ACTIVE);
        user.setLastLoginAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new ResponseUser(savedUser);
    }

    @Override
    @Transactional
    public ResponseUser updatePersonalInformation(String userId, UserUpdateRequest request) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        User user = userRepository.findByUserId(userId);

        String encodedPassword = passwordEncoder.encode(request.userPassword());
        user.setUserPassword(encodedPassword);
        user.setUserName(request.userName());
        user.setUserPhoneNumber(request.userPhoneNumber());
        user.setUserEmail(request.userEmail());
        user.setUserBirth(request.userBirth());

        return new ResponseUser(user);
    }

    @Override
    @Transactional
    public ResponseUser updateLastLoginAt(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updateLastLoginByUserId(userId, LocalDateTime.now());

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    @Transactional
    public ResponseUser updatePoint(String userId, int point) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updatePointByUserId(userId, point);

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    @Transactional
    public ResponseUser updateUserStatus(String userId, User.Status status) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updateStatusByUserId(userId, status);

        return new ResponseUser(userRepository.findByUserId(userId));
    }

    @Override
    @Transactional
    public ResponseUser updateUserGradeName(String userId, String gradeName) {
        if(!userRepository.existsById(userId)){
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
    @Transactional
    public void deleteUser(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.deleteById(userId);
    }
}
