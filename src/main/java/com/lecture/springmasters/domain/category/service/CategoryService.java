package com.lecture.springmasters.domain.category.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lecture.springmasters.common.service.RedisService;
import com.lecture.springmasters.domain.category.dto.CategoryRequest;
import com.lecture.springmasters.domain.category.dto.CategoryResponse;
import com.lecture.springmasters.domain.order.dto.CategoryProductCountRequest;
import com.lecture.springmasters.domain.order.dto.CategoryProductCountResponse;
import com.lecture.springmasters.entity.Category;
import com.lecture.springmasters.repository.CategoryQueryRepository;
import com.lecture.springmasters.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private static final String CACHE_KEY_CATEGORY_STRUCT = "category_struct";
  private static final String CACHE_KEY_PREFIX = "category_product_count";

  private final CategoryRepository categoryRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final RedisService redisService;

  public Boolean save(CategoryRequest request) {
    categoryRepository.save(
        Category.builder()
            .name(request.getName())
            .build()
    );
    return true;
  }

  public List<CategoryProductCountResponse> productCounts(
      @RequestParam CategoryProductCountRequest search) {
    String cacheKey = CACHE_KEY_PREFIX + search.hashCode();

    List<CategoryProductCountResponse> cachedResponse = redisService.findObject(cacheKey,
        new TypeReference<List<CategoryProductCountResponse>>() {
        });

    if (!ObjectUtils.isEmpty(cachedResponse)) {
      return cachedResponse;
    }

    List<CategoryProductCountResponse> responses = categoryQueryRepository.productCount(search);
    if (!ObjectUtils.isEmpty(responses)) {
      redisService.setObject(cacheKey, responses, 3600);
    }

    return responses;
  }

  public List<Category> getAll() {
    List<Category> categories = categoryRepository.findAll();
    return categories;
  }

  public List<CategoryResponse> findCategoryStruct() {
    List<Category> categories = categoryRepository.findAll();

    Map<Long, CategoryResponse> categoryResponseMap = new HashMap<>();
    for (Category category : categories) {
      CategoryResponse response = CategoryResponse.builder()
          .name(category.getName())
          .children(new ArrayList<>())
          .build();

      categoryResponseMap.put(category.getId(), response);
    }

    List<CategoryResponse> rootCategories = new ArrayList<>();
    for (Category category : categories) {
      CategoryResponse categoryResponse = categoryResponseMap.get(category.getId());

      if (ObjectUtils.isEmpty(category.getParent())) {
        rootCategories.add(categoryResponse);
      } else {
        CategoryResponse parentCategoryResponse = categoryResponseMap.get(
            category.getParent().getId());
        parentCategoryResponse.getChildren().add(categoryResponse);
      }
    }

    return rootCategories;
  }

  public List<CategoryResponse> findCategoryStructCacheAside() {
    List<CategoryResponse> cachedCategories = redisService.findObject(CACHE_KEY_CATEGORY_STRUCT,
        new TypeReference<List<CategoryResponse>>() {
        });

    if (!ObjectUtils.isEmpty(cachedCategories)) {
      return cachedCategories;
    }

    List<CategoryResponse> rootCategories = findCategoryStruct();

    redisService.setObject(CACHE_KEY_CATEGORY_STRUCT, rootCategories, 3600);

    return rootCategories;
  }

  //캐시와 DB 동시 업데이트
  public Boolean saveWriteThrough(CategoryRequest request) {
    Category category = Category.builder()
        .name(request.getName())
        .build();

    //카데고리 저장하면
    categoryRepository.save(category);
    //카테고리 구조를 cache에 덮어씌운다.
    updateCache();
    return true;
  }

  public void updateCache() {
    try {
      List<CategoryResponse> rootCategories = findCategoryStruct();
      redisService.setObject(CACHE_KEY_CATEGORY_STRUCT, rootCategories, 3600);

    } catch (Exception e) {
      log.error("Error update cache: {}", CACHE_KEY_CATEGORY_STRUCT, e);
    }
  }

  //데이터 쓰기 요청 시, 캐시에만 데이터를 업데이트.
  public Boolean saveWriteBack(CategoryRequest request) {
    List<CategoryResponse> categories = redisService.findObject(CACHE_KEY_CATEGORY_STRUCT,
        new TypeReference<List<CategoryResponse>>() {
        });

    if (ObjectUtils.isEmpty(categories)) {
      categories = new ArrayList<>();
    }

    CategoryResponse newCategory = CategoryResponse.builder()
        .name(request.getName())
        .children(new ArrayList<>())
        .build();

    categories.add(newCategory);
    redisService.setObject(CACHE_KEY_CATEGORY_STRUCT, categories, 3600);

    saveToDatabaseAsync(request);
    return true;
  }

  //나중에 이벤트큐로 이 비동기를 실행하는게 목적이다.
  @Async
  public void saveToDatabaseAsync(CategoryRequest request) {
    try {
      categoryRepository.save(Category.builder()
          .name(request.getName())
          .build()
      );
    } catch (Exception e) {
      //캐시 초기화 방법도 고려해볼만 함
      log.error("Error updating date: {}", e.getMessage(), e);
    }
  }

}
