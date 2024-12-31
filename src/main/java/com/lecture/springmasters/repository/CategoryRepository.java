package com.lecture.springmasters.repository;

import com.lecture.springmasters.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.lecture.springmasters.domain.repository fileName       : CategoryRepository
 * author         : LEE KYUHEON date           : 24. 12. 31. description    :
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
