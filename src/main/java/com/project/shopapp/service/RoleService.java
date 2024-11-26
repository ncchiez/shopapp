package com.project.shopapp.service;

import com.project.shopapp.dto.RoleDTO;
import com.project.shopapp.entity.Role;
import com.project.shopapp.enums.ROLE;
import com.project.shopapp.mapper.RoleMapper;
import com.project.shopapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public Role create(RoleDTO request) {
        var role = roleMapper.toRole(request);
//        var permissions = permissionReposiory.findAllById(request.getPermissions());
//        role.setPermissions(new HashSet<>(permissions));

        return (roleRepository.save(role));
    }

    public List<Role> findAll() {

        return roleRepository.findAll().stream().toList();
    }

    public void delete(String name){
        roleRepository.deleteById(name);
    }

}
