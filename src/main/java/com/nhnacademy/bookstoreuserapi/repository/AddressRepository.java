package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Address;
import com.nhnacademy.bookstoreuserapi.repository.queryfactory.AddressRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long>, AddressRepositoryCustom {
    boolean existsByUser_UserIdAndAddressDetail(String userId, String addressDetail);

    long countByUser_UserId(String userId);
}
