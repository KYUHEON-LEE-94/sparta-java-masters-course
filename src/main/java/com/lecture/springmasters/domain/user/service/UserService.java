package com.lecture.springmasters.domain.user.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.user.dao.UserMapperRepository;
import com.lecture.springmasters.domain.user.dto.UserDto;
import com.lecture.springmasters.domain.user.dto.UserRequest;
import com.lecture.springmasters.domain.user.dto.UserResponse;
import com.lecture.springmasters.domain.user.mapper.UserMapper;
import com.lecture.springmasters.entity.User;
import com.lecture.springmasters.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

  //MyBatis

  public Map<String, Object> findById(Long id) {
    return userMapperRepository.selectUserById(id);
  }

  @Transactional
  public List<UserDto> findUserByEmail(String email) {
    List<UserDto> user = userMapperRepository.selectUserByEmail(email);
    log.info("user : {}", user.size());
    return user;
  }

  @Transactional
  public void getUserByNameAndCreatedAt() {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "김철수1");
    params.put("createdAt", LocalDateTime.now());

    UserDto userMap = userMapperRepository.selectUserByMap(params);
    UserDto user = userMapperRepository.selectUserByNameAndCreatedAt("김철수1", LocalDateTime.now());

    log.info("user : {}", user.toString());
    log.info("userMap : {}", userMap.toString());
  }

  @Transactional
  public void getUserByRequest() {
    UserRequest userRequest = UserRequest.builder()
        .email("2@naver.com")
        .name("김철수2")
        .build();

    UserDto user = userMapperRepository.selectUserByRequest(userRequest);
    log.info("user : {}", user.toString());
  }

  @Transactional
  public void searchUserIf() {
    UserRequest userRequest = UserRequest.builder()
        .name("김철수3")
        .build();

    List<UserDto> user = userMapperRepository.searchUserIf(userRequest);
    log.info("user : {}", user.toString());
  }

  @Transactional
  public void searchUserChoose() {
    UserRequest userRequest = UserRequest.builder()
        .email("4@naver.com")
        .name("김철수4")
        .build();

    List<UserDto> user = userMapperRepository.searchUserChoose(userRequest);
    log.info("user : {}", user.toString());
  }

  @Transactional
  public void searchUserForeach() {
    List<UserRequest> userRequests = new ArrayList<>();

    userRequests.add(UserRequest.builder()
        .email("5@naver.com")
        .name("김철수5")
        .build());

    userRequests.add(UserRequest.builder()
        .email("6@naver.com")
        .name("김철수6")
        .build());

    List<UserDto> user = userMapperRepository.searchUserForeach(userRequests);
    log.info("user : {}", user.toString());
  }

  @Transactional
  public void searchUserPage() {
    Long total = userMapperRepository.searchUsersTotal();
    List<UserDto> users = userMapperRepository.searchUsersPage(10, 0);

    log.info("total : {} user : {}", total, users.toString());
  }

  @Transactional
  public void insertUser() {
    List<UserRequest> userRequests = new ArrayList<>();

    userRequests.add(UserRequest.builder()
        .email("#!@naver.com")
        .name("김철수#!")
        .password("123456")
        .build());

    userRequests.add(UserRequest.builder()
        .email("!Q@#@naver.com")
        .name("김철수!Q@#")
        .password("123456")
        .build());

    userMapperRepository.insertUser(userRequests);
  }

  @Transactional
  public void updateUser() {
    userMapperRepository.updateUsersEmails(LocalDateTime.now());
  }

  //JPA

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
