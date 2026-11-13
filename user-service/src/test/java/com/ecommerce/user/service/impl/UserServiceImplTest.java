package com.ecommerce.user.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.ecommerce.user.dto.response.UserResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class) // JUnit test ke saath Mockito initialize karta hai.
class UserServiceImplTest {

    @Mock // Fake repository: test mein real database call nahi hogi.
    private UserRepository userRepository;

    @Mock // Fake mapper: is test mein mapping ki zarurat nahi hai.
    private UserMapper userMapper;

    @InjectMocks // Dono mocks ko UserServiceImpl ke constructor mein inject karta hai.
    private UserServiceImpl userService;

    @Test
        // JUnit ko batata hai ki ye ek test method hai.
    void getUserById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {

        // Arrange: test ke liye ID aur expected repository behavior set karo.
        Long userId = 999L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty()); // User nahi mila, isliye empty Optional.

        // Act + Assert: method call karo aur expected exception verify karo.
        assertThrows(
                ResourceNotFoundException.class, // Isi exception ki expectation hai.
                () -> userService.getUserById(userId)
        );

        // Verify: confirm karo ki service ne repository se isi ID ko search kiya.
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUserResponse()
    {

        // ARRANGE: user ki ID aur expected response taiyar karo.
        Long userId = 1L;

        // User Entity me @Getter/@Setter hain. Islea ye sellter available hai.
        User user = new User();
        user.setId(userId);

        // UserResponse per @Builder laga hai.
        // Isliye response ko builder se create kar rahe hain.
        // new UserResponse() use nahi kar rahe hain => UserResponse expectedResponse = new UserResponse();
        UserResponse expectedResponse = UserResponse.builder().id(userId).name("Test User").build();
        expectedResponse.setId(userId);

        // Mock repository ko configure kar rahe hain.
        // findById(1L) call hone par user return hona chahiye.
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock mapper ko configure kar rahe hain.
        // toResponse(user) call hone par expected response return ho.
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        //ACT: service ka actual method call kar rhe hain.
        UserResponse actualResponse = userService.getUserById(userId);

        // ASSERT: returned response ka ID aur name verify karo.
        assertEquals(userId, actualResponse.getId());
        assertEquals("Test User", actualResponse.getName());

        // VERIFY: respository pe expected ID se search hua tha.
        verify(userRepository).findById(userId);


    }
}