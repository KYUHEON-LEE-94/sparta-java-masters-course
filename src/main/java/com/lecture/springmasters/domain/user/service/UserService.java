package com.lecture.springmasters.domain.user.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.user.dto.UserRequest;
import com.lecture.springmasters.domain.user.dto.UserResponse;
import com.lecture.springmasters.domain.user.entity.User;
import com.lecture.springmasters.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.lecture.springmasters.domain.message.user.service fileName       :
 * UserService author         : LEE KYUHEON date           : 24. 12. 27. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 27.        LEE KYUHEON       최초 생성
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse create(UserRequest request) {
    User userRequest = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .passwordHash(request.getPasswordHash())
        .phoneNumber(request.getPhoneNumber())
        .role(request.getRole())
        .build();

    User user = userRepository.save(userRequest);

    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .createdAt(user.getCreatedAt())
        .build();
  }

  /*서비스에서는 get 이름을 사용해서 무조건 값이 있는 상태로 return 함을 명시*/
  public User getUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USERS));
  }

  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }


}
