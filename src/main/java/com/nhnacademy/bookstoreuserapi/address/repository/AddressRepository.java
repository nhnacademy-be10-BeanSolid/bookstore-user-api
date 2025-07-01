package com.nhnacademy.bookstoreuserapi.address.repository;

import com.nhnacademy.bookstoreuserapi.address.domain.Address;
import com.nhnacademy.bookstoreuserapi.address.repository.queryfactory.AddressRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long>, AddressRepositoryCustom {
    boolean existsByUser_UserIdAndAddressDetail(String userId, String addressDetail);

    long countByUser_UserId(String userId);
}
