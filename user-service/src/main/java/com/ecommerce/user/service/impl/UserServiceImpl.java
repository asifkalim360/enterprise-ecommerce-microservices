package com.ecommerce.user.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.integration.user.CreateUserFromAuthRequest;
import com.ecommerce.user.dto.request.CreateUserRequest;
import com.ecommerce.user.dto.request.UpdateUserRequest;
import com.ecommerce.user.dto.response.UserResponse;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserStatus;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(CreateUserRequest request) 
    {
        log.info("Create new user");
        User user = userMapper.toEntity(request);
        User savedUave = userRepository.save(user);
        return userMapper.toResponse(savedUave);
    }

    @Override
    public UserResponse createUserFromAuth(CreateUserFromAuthRequest request)
    {
        log.info("Create user profile for authUserId:{}", request.getAuthUserId());
        User user = userMapper.toEntityFromAuth(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id)
    {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request)
    {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userMapper.updateEntity(user, request);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deactivateUser(Long id)
    {
        log.info("Deactivating user with id: {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }
}
