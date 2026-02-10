package com.example.phegon.phegonBank.exceptions;

import com.example.phegon.phegonBank.res.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//These errors can be useful once your request reaches to the controller layer that's why it's inside controllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAllUnknownExceptions(Exception e){
        Response<?> response = Response.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> NotFoundExceptions(NotFoundException e){
        Response<?> response = Response.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<?>> BadRequestException(BadRequestException e){
        Response<?> response = Response.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsuffiecientBalanceException.class)
    public ResponseEntity<Response<?>> InsuffiecientBalanceException(InsuffiecientBalanceException e){
        Response<?> response = Response.builder()
                .statusCode(HttpStatus.INSUFFICIENT_STORAGE.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.INSUFFICIENT_STORAGE);
    }



}
