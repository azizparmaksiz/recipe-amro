package com.task.recipe.config;

import com.task.recipe.exception.RecipeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RecipeNotFoundException.class})
    @ResponseBody
    protected ResponseEntity<String> handleNotFoundException(RecipeNotFoundException ex,
                                                                             WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<String>(ex.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseBody
    protected ResponseEntity<String> handleRuntimeException(RuntimeException ex,
                                                             WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<String>(ex.toString(), HttpStatus.BAD_REQUEST);
    }

}
