package com.lecture.springmasters.domain.products.service;

import com.lecture.springmasters.domain.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.lecture.springmasters.domain.products.service fileName       :
 * ProductsService author         : LEE KYUHEON date           : 24. 12. 28. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 28.        LEE KYUHEON       최초 생성
 */
@Service
@RequiredArgsConstructor
public class ProductsService {

  private final ProductsRepository productsRepository;
}
