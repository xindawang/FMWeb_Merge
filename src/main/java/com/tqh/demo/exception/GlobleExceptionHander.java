package com.tqh.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobleExceptionHander {

    private final static Logger logger= LoggerFactory.getLogger(GlobleExceptionHander.class);

    @ExceptionHandler(DataException.class)
    @ResponseBody
    public String DataExceptionHandler(DataException e){
        logger.error(e.getMessage());
        return  e.getMsg();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String ExceptionHandler(Exception e){
        logger.error(e.getMessage(),e);
        return  "操作失败";
    }
}
