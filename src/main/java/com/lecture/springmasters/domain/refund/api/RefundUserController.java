package com.lecture.springmasters.domain.refund.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.refund.dto.RefundResponse;
import com.lecture.springmasters.domain.refund.service.RefundService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/{userId}/refund")
public class RefundUserController {

  private final RefundService refundService;

  @GetMapping
  public ApiResponse<List<RefundResponse>> findByUserId(@PathVariable Long userId) {
    return ApiResponse.Success(refundService.findByUserId(userId));
  }

}
