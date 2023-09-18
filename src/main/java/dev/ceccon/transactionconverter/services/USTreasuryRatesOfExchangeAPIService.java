package dev.ceccon.transactionconverter.services;

import dev.ceccon.transactionconverter.entities.ConvertedTransaction;
import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.integration.ustreasuryapi.USTreasuryRatesOfExchangeAPIClient;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class USTreasuryRatesOfExchangeAPIService implements ConversionService {

    private final TransactionService transactionService;

    private final USTreasuryRatesOfExchangeAPIClient apiClient;

    public USTreasuryRatesOfExchangeAPIService(TransactionService transactionService, USTreasuryRatesOfExchangeAPIClient apiClient) {
        this.transactionService = transactionService;
        this.apiClient = apiClient;
    }

    @Override
    public ConvertedTransaction convertForCountryCurrency(Long transactionId, String country, String currency) {
        Transaction transaction = transactionService.findById(transactionId);
        String countryCurrency = WordUtils.capitalizeFully(country) + "-" + currency;
        BigDecimal conversionRate = apiClient.getConversationRateForCountryCurrency(transaction, countryCurrency);

        return new ConvertedTransaction(transaction, countryCurrency, conversionRate);
    }

    @Override
    public ConvertedTransaction convertForBaseCurrency(Long transactionId, String baseCurrency) {
        Transaction transaction = transactionService.findById(transactionId);
        BigDecimal conversionRate = apiClient.getConversationRateForBaseCurrency(transaction, baseCurrency);

        return new ConvertedTransaction(transaction, baseCurrency, conversionRate);
    }

}
