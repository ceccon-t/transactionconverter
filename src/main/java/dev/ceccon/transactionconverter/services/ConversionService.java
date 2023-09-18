package dev.ceccon.transactionconverter.services;

import dev.ceccon.transactionconverter.entities.ConvertedTransaction;

public interface ConversionService {

    ConvertedTransaction convertForBaseCurrency(Long transactionId, String baseCurrency);

    ConvertedTransaction convertForCountryCurrency(Long transactionId, String country, String currency);
}
