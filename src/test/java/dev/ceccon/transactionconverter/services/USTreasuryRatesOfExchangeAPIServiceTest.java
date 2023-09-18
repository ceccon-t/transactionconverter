package dev.ceccon.transactionconverter.services;

import dev.ceccon.transactionconverter.entities.ConvertedTransaction;
import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.integration.ustreasuryapi.USTreasuryRatesOfExchangeAPIClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class USTreasuryRatesOfExchangeAPIServiceTest {

    @InjectMocks
    USTreasuryRatesOfExchangeAPIService service;

    @Mock
    TransactionService transactionService;

    @Mock
    USTreasuryRatesOfExchangeAPIClient apiClient;

    @Test
    void testConvertTransactionWithExchangeRateForCountryCurrency() {
        Long desiredId = 1L;
        BigDecimal originalAmount = new BigDecimal("10.00");
        BigDecimal exchangeRate = new BigDecimal("3.2");
        BigDecimal expectedConvertedAmount = new BigDecimal("32.00");
        Transaction persistedTransaction = new Transaction(desiredId, "Transaction1", LocalDate.now(), originalAmount);

        given(transactionService.findById(desiredId)).willReturn(persistedTransaction);

        given(apiClient.getConversationRateForCountryCurrency(any(), any())).willReturn(exchangeRate);

        ConvertedTransaction converted = service.convertForCountryCurrency(desiredId, "Brazil", "Real");

        assertThat(converted).isNotNull();
        assertThat(converted.getId()).isEqualTo(persistedTransaction.getId());
        assertThat(converted.getDescription()).isEqualTo(persistedTransaction.getDescription());
        assertThat(converted.getDate()).isEqualTo(persistedTransaction.getDate());
        assertThat(converted.getAmountInUSDollars()).isEqualTo(persistedTransaction.getAmountInUSDollars());
        assertThat(converted.getExchangeRate()).isEqualTo(exchangeRate);
        assertThat(converted.getConvertedAmount()).isEqualTo(expectedConvertedAmount);

    }

    @Test
    void testConvertTransactionWithExchangeRateForBaseCurrency() {
        Long desiredId = 1L;
        BigDecimal originalAmount = new BigDecimal("10.00");
        BigDecimal exchangeRate = new BigDecimal("3.2");
        BigDecimal expectedConvertedAmount = new BigDecimal("32.00");
        Transaction persistedTransaction = new Transaction(desiredId, "Transaction1", LocalDate.now(), originalAmount);

        given(transactionService.findById(desiredId)).willReturn(persistedTransaction);

        given(apiClient.getConversationRateForBaseCurrency(any(), any())).willReturn(exchangeRate);

        ConvertedTransaction converted = service.convertForBaseCurrency(desiredId, "Real");

        assertThat(converted).isNotNull();
        assertThat(converted.getId()).isEqualTo(persistedTransaction.getId());
        assertThat(converted.getDescription()).isEqualTo(persistedTransaction.getDescription());
        assertThat(converted.getDate()).isEqualTo(persistedTransaction.getDate());
        assertThat(converted.getAmountInUSDollars()).isEqualTo(persistedTransaction.getAmountInUSDollars());
        assertThat(converted.getExchangeRate()).isEqualTo(exchangeRate);
        assertThat(converted.getConvertedAmount()).isEqualTo(expectedConvertedAmount);

    }

}