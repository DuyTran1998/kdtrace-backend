package com.duytran.kdtrace.mapper;

import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel userToUserModel(User user);

    User userModelToUser(UserModel userModel);
}