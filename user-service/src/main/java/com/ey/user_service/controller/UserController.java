package com.ey.user_service.controller;

import com.ey.user_service.repository.UserProfileRepository;
import com.ey.user_service.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileRepository repo;

    @PostMapping
    public UserProfile create (@RequestBody UserProfile user){
        return repo.save(user);
    }

    @GetMapping
    public List<UserProfile> all(){
        return repo.findAll();
    }
}
