package com.lecture.springmasters.domain.product.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.entity.Category;
import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.repository.CategoryRepository;
import com.lecture.springmasters.repository.ProductRepository;
import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBatchService {

  private final CategoryRepository categoryRepository;

  private final JdbcTemplate jdbcTemplate;
  private final ProductRepository productRepository;

  public void saveProductFromCsv(Long categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    try {
      ClassPathResource classPathResource = new ClassPathResource("products_sample_v1.csv");
      CSVReader csvReader = new CSVReader(
          new InputStreamReader(classPathResource.getInputStream()));

      List<String[]> records = csvReader.readAll();
      List<Product> products = new ArrayList<>();

      for (int i = 1; i < records.size(); i++) {
        String[] record = records.get(i);
        String name = record[0];
        String description = record[1];
        BigDecimal price = new BigDecimal(record[2]);
        Integer stock = Integer.valueOf(record[3]);

        products.add(Product.builder()
            .name(name)
            .description(description)
            .price(price)
            .stock(stock)
            .category(category)
            .build());
      }

      saveAll(products);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private void saveAll(List<Product> products) {
    String sql =
        "INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at) "
            + "VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

    List<Object[]> batchArgs = new ArrayList<>();
    for (Product product : products) {
      Object[] args = new Object[]{
          product.getName(),
          product.getDescription(),
          product.getPrice(),
          product.getStock(),
          product.getCategory().getId()
      };
      batchArgs.add(args);
    }

    jdbcTemplate.batchUpdate(sql, batchArgs);
    log.info("Successfully inserted {} records", batchArgs.size());
    log.info("query: {} ", sql);
  }
}
