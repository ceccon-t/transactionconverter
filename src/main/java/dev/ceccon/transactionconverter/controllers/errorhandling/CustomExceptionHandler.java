package dev.ceccon.transactionconverter.controllers.errorhandling;

import dev.ceccon.transactionconverter.exceptions.NoSuitableExchangeRateException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuitableExchangeRateException.class)
    public ResponseEntity<?> handleNoSuitableExchangeRateException(final NoSuitableExchangeRateException e, final HttpServletRequest request) {
        ErrorDTO dto = new ErrorDTO("No suitable exchange rate to convert transaction value was found");
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }
}
