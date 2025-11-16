package sfs.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sfs.service.RentalException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        String fieldName = ex.getBindingResult().getFieldErrors().getFirst().getField();
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        errors.put(fieldName, errorMessage);
        return errors;
    }

    @ExceptionHandler({RentalException.class, Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBusinessExceptions(Exception ex){
        Map<String, String> error = new HashMap<>();

        error.put("error", ex.getMessage());
        return error;
    }
}
