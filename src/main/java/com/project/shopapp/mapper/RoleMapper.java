package com.project.shopapp.mapper;

import com.project.shopapp.dto.RoleDTO;
import com.project.shopapp.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleDTO request);
}
