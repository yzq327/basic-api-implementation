package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.logging.log4j.LogManager;

import java.util.logging.Logger;


@ControllerAdvice
public class RsEventHandler {
   // private Logger logger = (Logger) LoggerFactory.getLogger(RsController.class);
    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage;
        if(e instanceof MethodArgumentNotValidException) {
            errorMessage = "invalid user";
           // logger.error("An error Message");
        } else {
            errorMessage = e.getMessage();
            //logger.error(errorMessage)
        }
        Error error=new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
