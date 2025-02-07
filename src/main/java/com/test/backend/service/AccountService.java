package com.test.backend.service;

import com.test.backend.dao.UserRepository;
import com.test.backend.entity.Account;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AccountService implements UserDetailsService {

    UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("No such user"));
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .build();
    }
}
