package dev.ceccon.transactionconverter.integration.ustreasuryapi;

import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.exceptions.NoSuitableExchangeRateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class USTreasuryRatesOfExchangeAPIClientTest {

    @InjectMocks
    USTreasuryRatesOfExchangeAPIClient apiClient;

    @Mock
    RestTemplate restTemplate;

    @Test
    void testGetConversionRateForCountryCurrency() {
        LocalDate transactionDate = LocalDate.of(2022, 2, 2);
        Transaction transaction = new Transaction(1L, "Transaction1", transactionDate, new BigDecimal("12.00"));
        BigDecimal closestExchangeRate = new BigDecimal("1.8");
        List<ExchangeRateRecordDTO> rateRecords = List.of(
                new ExchangeRateRecordDTO(closestExchangeRate, LocalDate.of(2022, 1, 2)),
                new ExchangeRateRecordDTO(new BigDecimal("1.7"), LocalDate.of(2021, 1, 2))
        );
        USTreasuryRatesOfExchangeAPIResponseDTO responseDTO = new USTreasuryRatesOfExchangeAPIResponseDTO(
                rateRecords,
                null,
                null
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(restTemplate.getForObject(captor.capture(), any())).willReturn(responseDTO);

        BigDecimal returnedRate = apiClient.getConversationRateForBaseCurrency(transaction, "Brazil - Real");

        assertThat(returnedRate).isEqualTo(closestExchangeRate);

        String urlIntercepted = captor.getValue();

        // Searching for exchange rates at transaction date or earlier
        assertThat(urlIntercepted.contains("record_date:lte:" + transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
        // Searching for at most 6 months earlier
        assertThat(urlIntercepted.contains("record_date:gte:" + transactionDate.minus(6, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_LOCAL_DATE)));
        // Searching for desired currency
        assertThat(urlIntercepted.contains("country_currency_desc:eq:Brazil-Real"));
        // Getting back results in descending date order
        assertThat(urlIntercepted.contains("sort=-record_date"));
    }

    @Test
    void testGetConversionRateForCountryCurrencyThrowsExceptionIfNoSuitableFound() {
        assertThrows(NoSuitableExchangeRateException.class, () -> {
            LocalDate transactionDate = LocalDate.of(2022, 2, 2);
            Transaction transaction = new Transaction(1L, "Transaction1", transactionDate, new BigDecimal("12.00"));
            USTreasuryRatesOfExchangeAPIResponseDTO responseDTO = new USTreasuryRatesOfExchangeAPIResponseDTO(
                    List.of(),
                    null,
                    null
            );
            given(restTemplate.getForObject(any(String.class), any())).willReturn(responseDTO);
            BigDecimal returnedRate = apiClient.getConversationRateForCountryCurrency(transaction, "Brazil-Real");
        });
    }

    @Test
    void testGetConversionRateForBaseCurrency() {
        LocalDate transactionDate = LocalDate.of(2022, 2, 2);
        Transaction transaction = new Transaction(1L, "Transaction1", transactionDate, new BigDecimal("12.00"));
        BigDecimal closestExchangeRate = new BigDecimal("1.8");
        List<ExchangeRateRecordDTO> rateRecords = List.of(
                new ExchangeRateRecordDTO(closestExchangeRate, LocalDate.of(2022, 1, 2)),
                new ExchangeRateRecordDTO(new BigDecimal("1.7"), LocalDate.of(2021, 1, 2))
        );
        USTreasuryRatesOfExchangeAPIResponseDTO responseDTO = new USTreasuryRatesOfExchangeAPIResponseDTO(
                rateRecords,
                null,
                null
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(restTemplate.getForObject(captor.capture(), any())).willReturn(responseDTO);

        BigDecimal returnedRate = apiClient.getConversationRateForBaseCurrency(transaction, "Real");

        assertThat(returnedRate).isEqualTo(closestExchangeRate);

        String urlIntercepted = captor.getValue();

        // Searching for exchange rates at transaction date or earlier
        assertThat(urlIntercepted.contains("record_date:lte:" + transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
        // Searching for at most 6 months earlier
        assertThat(urlIntercepted.contains("record_date:gte:" + transactionDate.minus(6, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_LOCAL_DATE)));
        // Searching for desired currency
        assertThat(urlIntercepted.contains("currency:eq:Real"));
        // Getting back results in descending date order
        assertThat(urlIntercepted.contains("sort=-record_date"));
    }

    @Test
    void testGetConversionRateForBaseCurrencyThrowsExceptionIfNoSuitableFound() {
        assertThrows(NoSuitableExchangeRateException.class, () -> {
            LocalDate transactionDate = LocalDate.of(2022, 2, 2);
            Transaction transaction = new Transaction(1L, "Transaction1", transactionDate, new BigDecimal("12.00"));
            USTreasuryRatesOfExchangeAPIResponseDTO responseDTO = new USTreasuryRatesOfExchangeAPIResponseDTO(
                    List.of(),
                    null,
                    null
            );
            given(restTemplate.getForObject(any(String.class), any())).willReturn(responseDTO);
            BigDecimal returnedRate = apiClient.getConversationRateForBaseCurrency(transaction, "Real");
        });
    }


}