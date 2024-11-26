package com.project.shopapp.controller;


import com.project.shopapp.dto.RoleDTO;
import com.project.shopapp.entity.Role;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    ApiResponse<Role> create(@RequestBody RoleDTO request) {

        ApiResponse<Role> apiResponse = new ApiResponse<>();
        apiResponse.setPayload(roleService.create(request));
        return apiResponse;

    }

    @GetMapping
    ApiResponse<List<Role>> findAll() {
        var apiResponse = new ApiResponse<List<Role>>();
        apiResponse.setPayload(roleService.findAll());
        return apiResponse;

    }
    @DeleteMapping("/{id}")
    ApiResponse<String> delete(@PathVariable String id) {
        roleService.delete(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Permission has been deleted !");
        return apiResponse;

    }
}
