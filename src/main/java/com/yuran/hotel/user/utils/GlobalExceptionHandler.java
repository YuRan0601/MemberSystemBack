package com.yuran.hotel.user.utils;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * 處理DTO資料驗證失敗的Exception
	 * @param ex
	 * @return
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return Result.build(message, ResultCodeEnum.DTO_FIELD_INVALID);
    }
    
    /**
     * 處理JWT過期的Exception
     * @param ex
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public Result handleExpiredJwtException(ExpiredJwtException ex) {
        return Result.build(null, ResultCodeEnum.JWT_EXPIRED);
    }
    
    /**
     * 處理JWT無效的Exception
     * @param ex
     * @return
     */
    @ExceptionHandler(JwtException.class)
    public Result handleJwtException(JwtException ex) {
    	return Result.build(null, ResultCodeEnum.JWT_INVALID);
    }
    
    
}
