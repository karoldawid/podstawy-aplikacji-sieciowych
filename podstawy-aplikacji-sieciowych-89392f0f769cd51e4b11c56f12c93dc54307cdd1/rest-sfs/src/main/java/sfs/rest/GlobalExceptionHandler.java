package sfs.rest;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sfs.exception.ResourceConflictException;
import sfs.exception.ResourceNotFoundException;
import sfs.service.RentalException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Walidacja (@NotNull, @Size itp.) -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    // 2. Nie znaleziono zasobu -> 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ResourceNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    // 3. Konflikty (zajęty login, zajęty termin) -> 409 Conflict
    @ExceptionHandler({ResourceConflictException.class, DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(Exception ex) {
        // DuplicateKeyException to błąd z bazy danych (np. MongoDB unique index)
        if (ex instanceof DuplicateKeyException) {
            return Map.of("error", "Podana wartość (np. login) jest już zajęta.");
        }
        // ResourceConflictException to nasz błąd logiczny
        return Map.of("error", ex.getMessage());
    }

    // 4. Błędy biznesowe aplikacji (stare) -> 400 Bad Request
    @ExceptionHandler(RentalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleRentalException(RentalException ex){
        return Map.of("error", ex.getMessage());
    }

    // 5. Fallback dla wszystkich innych błędów -> 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneralException(Exception ex){
        ex.printStackTrace(); // Logujemy stack trace na konsolę
        return Map.of("error", "Wystąpił nieoczekiwany błąd serwera: " + ex.getMessage());
    }
}