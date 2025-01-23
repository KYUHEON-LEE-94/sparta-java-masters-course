package com.lecture.springmasters.domain.user.dao;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapperRepository {

  Map<String, Object> selectUserById(@Param("id") Long id);
}
