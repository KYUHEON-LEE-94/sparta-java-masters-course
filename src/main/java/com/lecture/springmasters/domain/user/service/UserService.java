package com.lecture.springmasters.domain.user.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.user.dao.UserMapperRepository;
import com.lecture.springmasters.domain.user.dto.UserRequest;
import com.lecture.springmasters.domain.user.dto.UserResponse;
import com.lecture.springmasters.domain.user.mapper.UserMapper;
import com.lecture.springmasters.entity.User;
import com.lecture.springmasters.repository.UserRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapperRepository userMapperRepository;

  public Map<String, Object> findById(Long id) {
    return userMapperRepository.selectUserById(id);
  }

  public User save(UserRequest request) {
    User userRequest = UserMapper.INSTANCE.toUser(request);
    return userRepository.save(userRequest);
  }

  public User getById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
  }

  public UserResponse getUserResponseById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    log.info("order size : {}", user.getOrders().size());
    return UserMapper.INSTANCE.toUserResponse(user);
  }

  public List<User> getAll() {
    List<User> users = userRepository.findAll();
    for (User user : users) {
      log.info("order size : {}", user.getOrders().size());
    }
    return users;
  }

  public List<User> getAllWithOrder() {
    List<User> users = userRepository.findAllWithOrder();
    for (User user : users) {
      log.info("order size : {}", user.getOrders().size());
    }
    return users;
  }

}
