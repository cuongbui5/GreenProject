package com.example.greenproject.exception;

import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerGlobal {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handlerNotFoundException(NotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handlerIllegalStateException(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // Lấy message từ lỗi đầu tiên
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Invalid input";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(),errorMessage)
        );
    }
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handlerRuntimeException(RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }




}
