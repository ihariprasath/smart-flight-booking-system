package com.ey.auth_service.service;

import com.ey.auth_service.entity.User;
import com.ey.auth_service.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRespository repo;

    public User register(User user){
        return repo.save(user);
    }

    public Optional<User> login(String email, String password){
        return repo.findByEmail(email).filter(u -> u.getPassword().equals(password));
    }
}

