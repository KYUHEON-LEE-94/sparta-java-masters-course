package com.lecture.springmasters.common.aop;

import com.lecture.springmasters.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

  @Pointcut("execution(* com.lecture.springmasters.domain..service..*(..))")
  public void serviceMethods() {
  }

  @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
  public void logException(ServiceException exception) {
    log.error("AfterThrowing : [{}] {}", exception.getCode(), exception.getMessage());
  }


  @Pointcut("execution(* com.lecture.springmasters.domain.order.service.OrderService.order(..))")
  public void orderProcessing() {
  }

  @AfterThrowing(pointcut = "orderProcessing()", throwing = "exception")
  public void logAndNotifyException(ServiceException exception) {
    log.error("환불 처리 중 예외 발생: {}", exception.getMessage(), exception);
    sendNotification(exception.getMessage());
  }

  private void sendNotification(String errorMessage) {
    // TODO : 외부 로깅 시스템에 전송 로직 추가
    log.info("에러 알림 전송 : {}", errorMessage);
  }
}
