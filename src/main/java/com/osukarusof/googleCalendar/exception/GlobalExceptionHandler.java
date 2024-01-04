package com.osukarusof.googleCalendar.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osukarusof.googleCalendar.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField().replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
            errorResponse.put(fieldName, fieldError.getDefaultMessage());
        });

        ResponseDto responseDto = ResponseDto
                .builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed. Check the request parameters.")
                .fieldErrors(errorResponse)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
