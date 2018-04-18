package com.tqh.demo.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class HttpAspect {
    private final static Logger logger= LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.tqh.demo.controller.IotMapController.*(..))")
    public void log(){
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest=attributes.getRequest();
        logger.info("url={}",httpServletRequest.getRequestURL());
        logger.info("method={}",httpServletRequest.getMethod());
        logger.info("ip={}",httpServletRequest.getRemoteAddr());
        logger.info("class_method={}",joinPoint.getSignature().getName());
        logger.info("Args={}",joinPoint.getArgs());
    }
}
