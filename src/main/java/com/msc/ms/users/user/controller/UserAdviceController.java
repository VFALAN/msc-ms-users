package com.msc.ms.users.user.controller;

import com.msc.ms.users.common.model.dto.ErrorResponse;
import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(assignableTypes = UserController.class)
public class UserAdviceController {
    @Counted(value = "users.error.validation", description = "error in users process")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerValidExceptions(MethodArgumentNotValidException pException) {
        final var response = ErrorResponse.builder().message("Error in validation of User creation").errors(
                pException.getBindingResult().getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage))).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
