package com.lecture.springmasters.common.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum RefundStatus {

  PANDING,
  APPROVED,
  FAILED,
  ;

}
