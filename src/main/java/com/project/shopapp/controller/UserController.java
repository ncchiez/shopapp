package com.project.shopapp.controller;


import com.project.shopapp.dto.UserDTO;
import com.project.shopapp.entity.User;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.response.UserResponse;
import com.project.shopapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserDTO request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        apiResponse.setPayload(userService.createUser(request));
        return apiResponse;

    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        apiResponse.setPayload(userService.getAll());
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        apiResponse.setPayload(userService.getUserById(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@ModelAttribute UserDTO request, @PathVariable String id){

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        apiResponse.setPayload(userService.update(request, id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setSuccess(true);
        userService.delete(id);
        return apiResponse;
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getInfo() {
        ApiResponse apiResponse = new ApiResponse();
        var userRes = userService.getMyInfo();

        apiResponse.setSuccess(true);
        apiResponse.setPayload(userRes);

        return apiResponse;
    }


}
