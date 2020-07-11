package com.exercise.product.aggregator.exception;

import com.exercise.product.aggregator.exception.domain.NotFound;
import com.exercise.product.aggregator.exception.domain.ServerError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public NotFound productNotFound(NotFoundException ex) {
        NotFound notFound = NotFound.builder()
                .errorMsg(ex.getMessage())
                .build();
        return notFound;
    }

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ServerError genericException(GenericException ex) {
        ServerError serverError = ServerError.builder()
                .errorMessage(ex.getMessage())
                .errCode(500)
                .build();
        return serverError;
    }

}


