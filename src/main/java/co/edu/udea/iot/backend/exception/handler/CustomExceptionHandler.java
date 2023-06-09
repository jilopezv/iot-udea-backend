package co.edu.udea.iot.backend.exception.handler;

import co.edu.udea.iot.backend.exception.DataDuplicatedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity handleConstraintValidationException(ConstraintViolationException ex, HttpServletRequest request) {
        System.out.println(request.getRequestURL());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DataDuplicatedException.class)
    protected ResponseEntity handleDataDuplicatedException(DataDuplicatedException ex, HttpServletRequest request) {
        System.out.println(request.getRequestURL());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }


}
