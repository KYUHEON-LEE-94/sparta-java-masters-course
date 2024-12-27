package com.lecture.springmasters.domain.message.service;

import com.lecture.springmasters.domain.message.dto.MessageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : heon
 * @Description : MessageRepository.java
 * @since : 24. 12. 27.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 12. 27.       heon         최초 생성
 *
 * <pre>
 */
public interface MessageRepository extends JpaRepository<MessageRequest, String> {
}
