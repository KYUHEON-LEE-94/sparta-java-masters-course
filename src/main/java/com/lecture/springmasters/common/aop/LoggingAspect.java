package com.lecture.springmasters.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Before("execution(* com.lecture.springmasters.domain..api..*(..))")
  public void logBeforeExecution() {
    log.info("[execution] 메서드 실행 전 로그");
  }

  @Before("within(com.lecture.springmasters.domain..*)")
  public void logBeforeWithin() {
    log.info("[within] 메서드 실행 전 로그");
  }

  @Before("@annotation(com.lecture.springmasters.common.annotation.Loggable)")
  public void logBeforeAnnotation() {
    log.info("[annotation] 메서드 실행 전 로그");
  }


  @Before("execution(* com.lecture.springmasters.domain..*(..))")
  public void logMethodDetails(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();

    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        log.info("전달된 파라미터 [{}]: {}", i, args[i]);
      }
    }

    log.info("실행된 메서드 이름: {}", joinPoint.getSignature().getName());
  }
}
