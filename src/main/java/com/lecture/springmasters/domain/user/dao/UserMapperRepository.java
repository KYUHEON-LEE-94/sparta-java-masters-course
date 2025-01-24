package com.lecture.springmasters.domain.user.dao;

import com.lecture.springmasters.domain.user.dto.UserDto;
import com.lecture.springmasters.domain.user.dto.UserRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapperRepository {

  Map<String, Object> selectUserById(@Param("id") Long id);

  List<UserDto> selectUserByEmail(@Param("email") String email);

  UserDto selectUserByNameAndCreatedAt(@Param("name") String name,
      @Param("createdAt") LocalDateTime createdAt);

  UserDto selectUserByMap(Map<String, Object> params);

  UserDto selectUserByRequest(UserRequest userRequest);

  List<UserDto> searchUserIf(UserRequest userRequest);

  List<UserDto> searchUserChoose(UserRequest userRequest);

  List<UserDto> searchUserForeach(List<UserRequest> userRequests);

  Long searchUsersTotal();

  List<UserDto> searchUsersPage(@Param("limit") Integer limit, @Param("offset") Integer offset);

  void insertUser(List<UserRequest> userRequests);

  void updateUsersEmails(@Param("createdAt") LocalDateTime createdAt);
}
