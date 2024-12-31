package com.lecture.springmasters.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * packageName    : com.lecture.springmasters.domain.entity fileName       : Category author : LEE
 * KYUHEON date           : 24. 12. 30. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 30.        LEE KYUHEON       최초 생성
 */
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name", nullable = false)
  String name;

  /*객체를 바라보게 되면 객체를 설명하는 단어를 사용*/
  @ManyToOne // ~~id가 들어가면 보통 ManyToOne
  @JoinColumn(name = "parent_id")
  Category parent;
//이걸 호출하면 하나의 엔티티와의 연결성을 나타내기 때문에 단수 return

  //실존하는 컬럼이 아니기 때문에 JoinColumn 필요없
  @OneToMany(mappedBy = "parent")
  List<Category> categories;
//내가 만약 이걸 호출하면 여러개의 category가 return 될거잖아? 그러니까 List 형태로 return 받는거지.

  /*category입장에서 내가 product에 쓰이고 있구나? 를 알려주는게 OneToMany*/
  @BatchSize(size = 10)
  @OneToMany(mappedBy = "category")
  @JsonBackReference
  List<Product> products;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  LocalDateTime updatedAt;

  @Builder
  public Category(
      String name
  ) {
    this.name = name;
  }
}
