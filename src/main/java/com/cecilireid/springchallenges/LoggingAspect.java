package com.cecilireid.springchallenges;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//TODO: Review more on Aspect Oriented Programming
@Component
@Aspect
public class LoggingAspect {
    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    @Around("execution(* com.cecilireid.springchallenges.CateringJobController.*ById(Long)) && args(id)")
    public Object logObjects(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("Received request to get job by id: {}", id);
        Object response = joinPoint.proceed();
        logger.info("Returned response for request: {}", response);
        return response;
    }
}
