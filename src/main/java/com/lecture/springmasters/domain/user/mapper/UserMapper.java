package com.lecture.springmasters.domain.user.mapper;

import com.lecture.springmasters.domain.user.dto.UserRequest;
import com.lecture.springmasters.domain.user.dto.UserResponse;
import com.lecture.springmasters.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * packageName    : com.lecture.springmasters.domain.user.mapper fileName       : UserMapper author
 *        : LEE KYUHEON date           : 24. 12. 28. description    : 지금 버전은 @AllArgsConstructor 가
 * 필수다. MapStruct은 버전에 따른 차이가 크다.
 * <p>
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 28.        LEE KYUHEON       최초 생성
 */

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


  UserResponse toResponse(User user);

  User toEntity(UserRequest userDto);
}
