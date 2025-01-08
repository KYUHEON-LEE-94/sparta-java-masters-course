package com.lecture.springmasters.domain.user.mapper;

import com.lecture.springmasters.domain.user.dto.UserRequest;
import com.lecture.springmasters.domain.user.dto.UserResponse;
import com.lecture.springmasters.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User toUser(UserRequest request);

  UserResponse toUserResponse(User user);
}
