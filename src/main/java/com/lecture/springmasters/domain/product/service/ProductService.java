package com.lecture.springmasters.domain.product.service;

import com.lecture.springmasters.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.lecture.springmasters.domain.product fileName       : ProductService author
 * : LEE KYUHEON date           : 24. 12. 31. description    :
 */
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  
}
