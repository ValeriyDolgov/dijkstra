package com.example.dijkstra.service;

import com.example.dijkstra.controller.request.CreateManagerRequest;
import com.example.dijkstra.controller.request.CreateUserRequest;
import com.example.dijkstra.model.User;
import com.example.dijkstra.repository.UserRepository;
import com.example.dijkstra.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void createDriverUser(CreateUserRequest createUserRequest) {
        var newUser = userMapper.createDriverUser(createUserRequest);
        newUser.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(newUser);
    }

    public void createManagerUser(CreateManagerRequest createUserRequest) {
        var newUser = userMapper.createManagerUser(createUserRequest);
        newUser.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByEmail(username);
    }

}
