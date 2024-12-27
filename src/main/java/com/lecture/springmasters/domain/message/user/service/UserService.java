package com.lecture.springmasters.domain.message.user.service;

import com.lecture.springmasters.domain.message.user.entity.User;
import com.lecture.springmasters.domain.message.user.repository.UserRepository;
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

  /*서비스에서는 get 이름을 사용해서 어떻든 값이 있는 상태로 return 함을 명시*/
  public void getUser() {
    User requestUser = User.builder().build();
    User user = userRepository.save(requestUser);
  }
}
