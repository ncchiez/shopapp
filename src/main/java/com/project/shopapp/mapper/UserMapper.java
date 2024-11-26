package com.project.shopapp.mapper;

import com.project.shopapp.dto.UserDTO;
import com.project.shopapp.entity.User;
import com.project.shopapp.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toUser(UserDTO request);
    //@Mapping(source = "firstName", target = "lastname") // firstName = lastName
    //@Mapping(target = "firstName", ignore = true) // ko map firstName
    UserResponse toUserResponse(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateUser(@MappingTarget User user, UserDTO request);
}
