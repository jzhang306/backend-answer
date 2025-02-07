package com.test.backend.dao;

import com.test.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);

}
