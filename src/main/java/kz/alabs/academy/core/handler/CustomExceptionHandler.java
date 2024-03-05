package kz.alabs.academy.core.handler;

import kz.alabs.academy.core.exception.BadRequestException;
import kz.alabs.academy.core.exception.NotFoundException;
import kz.alabs.academy.core.exception.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionResponse handleException(NotFoundException exception){
        log.error(exception.getMessage(), exception);
        return new ExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage(), "404");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ExceptionResponse handleException(BadRequestException exception){
        log.error(exception.getMessage(), exception);
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), "400");
    }

}
