package dev.ceccon.transactionconverter.controllers;

import dev.ceccon.transactionconverter.entities.ConvertedTransaction;
import dev.ceccon.transactionconverter.services.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConversionController {

    public static final String CONVERSION_ENDPOINT_BASE = "/api/v1/conversion";
    public static final String COUNTRY_CURRENCY_PREFIX = "/countryAndCurrency";
    public static final String BASE_CURRENCY_PREFIX = "/onlyCurrency";

    private final ConversionService service;


    public ConversionController(ConversionService service) {
        this.service = service;
    }

    @GetMapping(CONVERSION_ENDPOINT_BASE + COUNTRY_CURRENCY_PREFIX + "/{transactionId}/{country}/{currency}")
    public ConvertedTransaction handleConversionRequestForCountryCurrency(
            @PathVariable("transactionId") Long transactionId,
            @PathVariable("country") String country,
            @PathVariable("currency") String currency) {
        return service.convertForCountryCurrency(transactionId, country, currency);
    }

    @GetMapping(CONVERSION_ENDPOINT_BASE + BASE_CURRENCY_PREFIX + "/{transactionId}/{baseCurrency}")
    public ConvertedTransaction handleConversionRequestForBaseCurrency(
            @PathVariable("transactionId") Long transactionId,
            @PathVariable("baseCurrency") String baseCurrency) {
        return service.convertForBaseCurrency(transactionId, baseCurrency);
    }
}
