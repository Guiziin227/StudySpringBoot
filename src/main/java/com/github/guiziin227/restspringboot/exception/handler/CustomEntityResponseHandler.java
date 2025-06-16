package com.github.guiziin227.restspringboot.exception.handler;

import com.github.guiziin227.restspringboot.exception.BadRequestException;
import com.github.guiziin227.restspringboot.exception.ExceptionResponse;
import com.github.guiziin227.restspringboot.exception.FileNotFoundException;
import com.github.guiziin227.restspringboot.exception.FileStorageException;
import com.github.guiziin227.restspringboot.exception.RequiredObjectIsNullException;
import com.github.guiziin227.restspringboot.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse resp = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResourceNotFoundException.class, FileNotFoundException.class})
    public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse resp = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({RequiredObjectIsNullException.class, BadRequestException.class})
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FileStorageException.class})
    public final ResponseEntity<ExceptionResponse> handleFileStorageException(Exception ex, WebRequest request) {
        ExceptionResponse resp = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
