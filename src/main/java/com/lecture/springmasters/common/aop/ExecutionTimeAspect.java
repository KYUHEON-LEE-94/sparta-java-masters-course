package com.lecture.springmasters.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

  @Pointcut("execution(* com.lecture.springmasters.domain..service..*(..))")
  public void serviceMethods() {
  }

  @Around("serviceMethods()")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    log.info("{} 메서드 실행 시간: {} ms", joinPoint.getSignature(), (endTime - startTime));

    return result;
  }

  @Pointcut("execution(* com.lecture.springmasters.domain.order.service.OrderService.order(..))")
  public void orderProcessing() {
  }

  @Around("orderProcessing()")
  public Object orderMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    log.info("주문 처리 시작: {}", joinPoint.getSignature());

    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    log.info("주문 처리 완료: {} 실행 시간: {} ms", joinPoint.getSignature(), (endTime - startTime));

    return result;
  }

}
