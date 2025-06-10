package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser_UserId(String userId);

    boolean existsByUser_UserIdAndAddressDetail(String userId, String addressDetail);

    long countByUser_UserId(String userId);
}
