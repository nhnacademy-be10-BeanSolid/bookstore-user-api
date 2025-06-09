package com.nhnacademy.bookstoreuserapi.service.impl;

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

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findById(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId + " : 해당 유저를 찾을 수 없습니다.");
        }

        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public void saveUser(User user) {

        if(userRepository.existsById(user.getUserId())){
            throw new UserAlreadyExistException(user.getUserId() + " : 해당 아이디를 가진 유저가 이미 존재합니다.");
        }

        String encodedPassword = passwordEncoder.encode(user.getUserPassword());

        user.setUserPassword(encodedPassword);
        user.setLastLoginAt(LocalDateTime.now());
        user.setUserPoint(0);
        user.setAuth(false);
        user.setUserStatus(User.Status.ACTIVE);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePersonalInformation(User user) {

        if(!userRepository.existsById(user.getUserId())){
            throw new UserNotFoundException(user.getUserId() + " : 해당 유저를 찾을 수 없습니다.");
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLastLoginAt(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId + " : 해당 유저를 찾을 수 없습니다.");
        }

        userRepository.updateLastLoginByUserId(userId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void updatePoint(String userId, int point) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId + " : 해당 유저를 찾을 수 없습니다.");
        }

        userRepository.updatePointByUserId(userId, point);
    }

    @Override
    @Transactional
    public void updateUserStatus(String userId, User.Status status) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId + " : 해당 유저를 찾을 수 없습니다.");
        }

        userRepository.updateStatusByUserId(userId, status);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId + " : 해당 유저를 찾을 수 없습니다.");
        }

        userRepository.deleteById(userId);
    }
}
