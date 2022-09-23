package com.springweb.springweb.controller;

import com.springweb.springweb.exception.SpringException;
import com.springweb.springweb.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@ResponseBody()
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
//        MethodArgumentNotValidException
//        FieldError fieldError = e.getFieldError();
//        String field = fieldError.getField();
//        String defaultMessage = fieldError.getDefaultMessage();
//        Map<String, String> response = new HashMap<>();
//        response.put(field, defaultMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for(FieldError fieldError: e.getFieldErrors()){
            errorResponse.addValidation(fieldError.getField(),fieldError.getDefaultMessage());
        }

        return errorResponse;

    }

    @ResponseBody
    @ExceptionHandler(SpringException.class)
    public ResponseEntity<ErrorResponse> SpringException(SpringException e){
        int statusCode = e.getStatusCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(errorResponse);
        return  response;
    }



}
