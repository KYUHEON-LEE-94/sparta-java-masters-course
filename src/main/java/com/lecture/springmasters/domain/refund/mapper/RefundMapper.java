package com.lecture.springmasters.domain.refund.mapper;

import com.lecture.springmasters.domain.refund.dto.RefundResponse;
import com.lecture.springmasters.entity.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RefundMapper {

  RefundMapper INSTANCE = Mappers.getMapper(RefundMapper.class);

  RefundResponse toRefundResponse(Refund refund);
}
