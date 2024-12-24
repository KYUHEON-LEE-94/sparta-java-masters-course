package com.lecture.springmasters.domain.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.lecture.springmasters.domain.message.service fileName       :
 * MessageServiceImpl author         : LEE KYUHEON date           : 24. 12. 24. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 24.        LEE KYUHEON       최초 생성
 */

@Service
public class UserService {

  private List<Map<String, String>> userList = new ArrayList();
  private HashMap<String, String> users = new HashMap<>();

  public List<Map<String, String>> getUsers() {
    return userList;
  }

  public List<Map<String, String>> createUser(HashMap<String, String> users) {
    setUsers(users); // setUsers를 통해 필드를 설정
    userList.add(users);
    return userList;
  }

  public List<Map<String, String>> updateUser(String username, String password) {
    users.put(username, password);
    userList.add(users);
    return userList;
  }

  private void setUsers(HashMap<String, String> users) {
    this.users = users; // 외부에서 받은 HashMap을 내부 필드로 설정
  }
}
