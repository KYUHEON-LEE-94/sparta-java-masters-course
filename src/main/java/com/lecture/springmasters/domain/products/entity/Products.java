package com.lecture.springmasters.domain.products.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * packageName    : com.lecture.springmasters.domain.message.user.entity fileName       : User
 * author         : LEE KYUHEON date           : 24. 12. 27. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 27.        LEE KYUHEON       최초 생성
 */

@Getter
@Entity
@DynamicInsert //createAt 동작
@DynamicUpdate //updateAt 동작
@Table(name = "products")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Products {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Setter
  @Column(nullable = false)
  String name;

  @Column(nullable = false, unique = true)
  int price;

  @Column(nullable = false)
  int stock;

  @Column(length = 15)
  String categoryId;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  LocalDateTime updatedAt;


  @Builder
  public Products(String categoryId, int stock, int price, String name) {
    this.categoryId = categoryId;
    this.stock = stock;
    this.price = price;
    this.name = name;
  }
}
