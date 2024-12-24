package com.lecture.springmasters.domain.message.controller;

import com.lecture.springmasters.domain.message.service.MessageService;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.lecture.springmasters.domain.message.controller fileName       :
 * MessageController author         : LEE KYUHEON date           : 24. 12. 24. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 24.        LEE KYUHEON       최초 생성
 */
@RestController("/messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @GetMapping
  public ResponseEntity<?> findMessages() {
    return ResponseEntity.ok("message");
  }

  @PostMapping
  public ResponseEntity<?> createMessages(@RequestBody HashMap<String, String> message) {
    return ResponseEntity.ok("message");
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> createMessages(
      @PathVariable Long id,
      @RequestBody HashMap<String, String> message) {
    return ResponseEntity.ok("message");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteMessages(
      @PathVariable Long id) {
    return ResponseEntity.ok("message");
  }

}
