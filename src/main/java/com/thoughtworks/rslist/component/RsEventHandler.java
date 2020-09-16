package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@ControllerAdvice
public class RsEventHandler {
    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage;
        if(e instanceof MethodArgumentNotValidException) {
            errorMessage = "invalid user";
        } else {
            errorMessage = e.getMessage();
        }
        Error error=new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
