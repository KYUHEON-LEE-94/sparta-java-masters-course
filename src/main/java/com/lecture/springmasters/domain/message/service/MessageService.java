package com.lecture.springmasters.domain.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.lecture.springmasters.domain.message.service fileName       :
 * MessageServiceImpl author         : LEE KYUHEON date           : 24. 12. 24. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 24.        LEE KYUHEON       최초 생성
 */

@Service
@RequiredArgsConstructor
public class MessageService {


  public String getMessage() {
    return "Hello, Spring IoC and DI!";
  }
}
