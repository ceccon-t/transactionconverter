package dev.ceccon.transactionconverter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No suitable exchange rate to convert transaction value was found")
public class NoSuitableExchangeRateException extends RuntimeException{

    public NoSuitableExchangeRateException() {
    }

    public NoSuitableExchangeRateException(String message) {
        super(message);
    }

    public NoSuitableExchangeRateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuitableExchangeRateException(Throwable cause) {
        super(cause);
    }

    public NoSuitableExchangeRateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
