package com.lecture.springmasters.domain.refund.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.refund.dto.RefundRequest;
import com.lecture.springmasters.domain.refund.dto.RefundResponse;
import com.lecture.springmasters.domain.refund.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("refund")
public class RefundController {

  private final RefundService refundService;

  @PostMapping
  public ApiResponse<RefundResponse> refund(@RequestBody RefundRequest request) {
    return ApiResponse.Success(refundService.refund(request));
  }
}