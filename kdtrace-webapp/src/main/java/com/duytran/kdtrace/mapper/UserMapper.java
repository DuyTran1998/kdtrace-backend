package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.dto.UserDto;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel userToUserModel(User user);

    User userModelToUser(UserModel userModel);

    List<UserModel> userListToUserModelList(List<User> users);

    List<User> userModelListToUserList(List<UserModel> userModel);

    @Mapping(target = "role", source = "role.roleName")
    UserDto userToUserDto (User user);
}
