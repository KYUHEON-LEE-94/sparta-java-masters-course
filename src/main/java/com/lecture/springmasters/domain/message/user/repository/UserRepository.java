package com.lecture.springmasters.domain.message.user.repository;

import com.lecture.springmasters.domain.message.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.lecture.springmasters.domain.message.user.repository fileName       :
 * UserRepository author         : LEE KYUHEON date           : 24. 12. 27. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 27.        LEE KYUHEON       최초 생성
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAllByUsernameOrEmailOrderByIdDesc(String username, String email);

  Optional<User> findByUsernameOrEmailOrderByIdDesc(String username, String email);
}
