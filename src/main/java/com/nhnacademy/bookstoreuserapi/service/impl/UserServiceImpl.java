package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeNotFoundException;
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
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserGradeRepository userGradeRepository;

    @Override
    public Optional<User> findById(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public void saveUser(User user) {

        if(userRepository.existsById(user.getUserId())){
            throw new UserAlreadyExistException(user.getUserId());
        }

        String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        UserGrade basicGrade = userGradeRepository.findByGradeName(BASIC);

        user.setUserPassword(encodedPassword);
        user.setLastLoginAt(LocalDateTime.now());
        user.setUserPoint(0);
        user.setAuth(false);
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserGrade(basicGrade);
        user.setOrderMoney(0);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePersonalInformation(User user) {

        if(!userRepository.existsById(user.getUserId())){
            throw new UserNotFoundException(user.getUserId());
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLastLoginAt(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updateLastLoginByUserId(userId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void updatePoint(String userId, int point) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updatePointByUserId(userId, point);
    }

    @Override
    @Transactional
    public void updateUserStatus(String userId, User.Status status) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.updateStatusByUserId(userId, status);
    }

    @Override
    @Transactional
    public void updateUserGradeName(String userId, String gradeName) {
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
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }

        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void updateOrderMoney(String userId, long orderMoney) {
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        userRepository.updateOrderMoneyByUserId(userId, orderMoney);

        UserGrade updatedGrade = userGradeRepository
                .findTopByRequiredMoneyLessThanEqualOrderByRequiredMoneyDesc(userRepository.findById(userId).get().getOrderMoney());

        if (updatedGrade != null) {
            userRepository.updateUserGrade_gradeNameByUserId(userId, updatedGrade.getGradeName());
        }
    }
}
