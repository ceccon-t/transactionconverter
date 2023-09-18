package dev.ceccon.transactionconverter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.integration.ustreasuryapi.ExchangeRateRecordDTO;
import dev.ceccon.transactionconverter.integration.ustreasuryapi.USTreasuryRatesOfExchangeAPIClient;
import dev.ceccon.transactionconverter.integration.ustreasuryapi.USTreasuryRatesOfExchangeAPIResponseDTO;
import dev.ceccon.transactionconverter.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static dev.ceccon.transactionconverter.controllers.ConversionController.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class UseCase02Test_ConvertTransaction {

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    USTreasuryRatesOfExchangeAPIClient apiClient;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @Test
    void convertExistingTransactionForCountryAndCurrency() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        BigDecimal originalAmount = new BigDecimal("10.00");
        BigDecimal closestRate = new BigDecimal("1.8");
        Transaction transaction = new Transaction(1L, "NewTransaction", LocalDate.of(2023, 9, 18), originalAmount);
        transactionRepository.save(transaction);
        List<ExchangeRateRecordDTO> rateRecords = List.of(
                new ExchangeRateRecordDTO(closestRate, LocalDate.of(2022, 1, 2)),
                new ExchangeRateRecordDTO(new BigDecimal("1.7"), LocalDate.of(2021, 1, 2))
        );
        USTreasuryRatesOfExchangeAPIResponseDTO responseDTO = new USTreasuryRatesOfExchangeAPIResponseDTO(
                rateRecords,
                null,
                null
        );

        given(restTemplate.getForObject(any(String.class), any())).willReturn(responseDTO);

        mockMvc.perform(get(CONVERSION_ENDPOINT_BASE + COUNTRY_CURRENCY_PREFIX + "/" + 1L + "/" + "Brazil" + "/" + "Real")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("NewTransaction")))
                .andExpect(jsonPath("$.date", is("2023-09-18")))
                .andExpect(jsonPath("$.amountInUSDollars", is (10.00)))
                .andExpect(jsonPath("$.targetCurrency", is("Brazil-Real")))
                .andExpect(jsonPath("$.exchangeRate", is(1.8)))
                .andExpect(jsonPath("$.convertedAmount", is(18.00)));
    }

    @Test
    void convertExistingTransactionForJustCurrency() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        BigDecimal originalAmount = new BigDecimal("10.00");
        BigDecimal closestRate = new BigDecimal("1.8");
        Transaction transaction = new Transaction(1L, "NewTransaction", LocalDate.of(2023, 9, 18), originalAmount);
        transactionRepository.save(transaction);
        List<ExchangeRateRecordDTO> rateRecords = List.of(
                new ExchangeRateRecordDTO(closestRate, LocalDate.of(2022, 1, 2)),
                new ExchangeRateRecordDTO(new BigDecimal("1.7"), LocalDate.of(2021, 1, 2))
        );
        USTreasuryRatesOfExchangeAPIResponseDTO responseDTO = new USTreasuryRatesOfExchangeAPIResponseDTO(
                rateRecords,
                null,
                null
        );

        given(restTemplate.getForObject(any(String.class), any())).willReturn(responseDTO);

        mockMvc.perform(get(CONVERSION_ENDPOINT_BASE + BASE_CURRENCY_PREFIX + "/" + 1L + "/" + "Real")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("NewTransaction")))
                .andExpect(jsonPath("$.date", is("2023-09-18")))
                .andExpect(jsonPath("$.amountInUSDollars", is (10.00)))
                .andExpect(jsonPath("$.targetCurrency", is("Real")))
                .andExpect(jsonPath("$.exchangeRate", is(1.8)))
                .andExpect(jsonPath("$.convertedAmount", is(18.00)));
    }

}