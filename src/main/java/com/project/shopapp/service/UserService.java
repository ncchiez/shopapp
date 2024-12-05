package com.project.shopapp.service;


import com.project.shopapp.dto.UserDTO;
import com.project.shopapp.entity.Role;
import com.project.shopapp.entity.User;
import com.project.shopapp.enums.ROLE;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.UserMapper;
import com.project.shopapp.repository.RoleRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    public UserResponse createUser(UserDTO request) {
        if(userRepository.existsByEmail(request.getEmail()) || userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(request);

        if(!request.getRetypePassword().equals(request.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByName(ROLE.USER.name());
        if (role == null) {
            // Nếu không tồn tại, tạo mới role USER
            role = new Role();
            role.setName(ROLE.USER.name());
            role = roleRepository.save(role); // Lưu role mới vào database
        }
//        HashSet<Role> roles = new HashSet<>();
//        roles.add(role);
        user.setRole(role);
        user.setActive(true);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getUserById(String id) {
        User user= userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("user not found !"));

//        boolean exists = user.getRole().stream()
//                .anyMatch(role -> "ADMIN".equals(role.getName()));
        var userRes = userMapper.toUserResponse(user);

        //userRes.setCheckAdmin(exists);


        return userRes;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        var list = userRepository.findAll();

        return list.stream().map(userMapper::toUserResponse).toList();
    }


    public UserResponse update(UserDTO request, String id) {
        User user= userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("user not found !"));

        Set<Role> roles = new HashSet<>();
        //log.info("Check box role:"+ request.isCheckAdmin());
//        if(request.isCheckAdmin()){
//            var role= roleRepository.findByName(ROLE.ADMIN.name());
//            roles.add(role);
//            user.setRoles(roles);
//        }else{
            var role= roleRepository.findByName(ROLE.USER.name());
            roles.add(role);
            user.setRole(role);
        //}
        userMapper.updateUser(user,request);
        //log.info("Role ne : "+user.getRoles().toString());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByEmail(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
