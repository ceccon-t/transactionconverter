package dev.ceccon.transactionconverter.controllers;

import dev.ceccon.transactionconverter.entities.ConvertedTransaction;
import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.services.ConversionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static dev.ceccon.transactionconverter.controllers.ConversionController.CONVERSION_ENDPOINT_BASE;
import static dev.ceccon.transactionconverter.controllers.ConversionController.COUNTRY_CURRENCY_PREFIX;
import static dev.ceccon.transactionconverter.controllers.ConversionController.BASE_CURRENCY_PREFIX;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConversionController.class)
class ConversionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ConversionService service;

    @Test
    void testConvertCountryCurrencyResponse() throws Exception {
        Long desiredId = 1L;
        Transaction foundTransaction = new Transaction(
                desiredId,
                "Transaction1",
                LocalDate.of(2023,9,18),
                new BigDecimal("10.00")
        );
        BigDecimal exchangeRate = new BigDecimal("1.6");
        String desiredCountry = "Brazil";
        String desiredCurrency = "Real";

        ConvertedTransaction converted = new ConvertedTransaction(
                foundTransaction, "Brazil-Real", exchangeRate
        );

        given(service.convertForCountryCurrency(any(), any(), any())).willReturn(converted);

        mockMvc.perform(get(CONVERSION_ENDPOINT_BASE + COUNTRY_CURRENCY_PREFIX + "/" + desiredId + "/" + desiredCountry + "/" + desiredCurrency)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(converted.getDescription())))
                .andExpect(jsonPath("$.date", is("2023-09-18")))
                .andExpect(jsonPath("$.amountInUSDollars", is(10.00)))
                .andExpect(jsonPath("$.targetCurrency", is("Brazil-Real")))
                .andExpect(jsonPath("$.exchangeRate", is(1.6)))
                .andExpect(jsonPath("$.convertedAmount", is(16.00)));

    }

    @Test
    void testConvertBaseCurrencyResponse() throws Exception {
        Long desiredId = 1L;
        Transaction foundTransaction = new Transaction(
                desiredId,
                "Transaction1",
                LocalDate.of(2023,9,18),
                new BigDecimal("10.00")
        );
        BigDecimal exchangeRate = new BigDecimal("1.6");
        String desiredCurrency = "Real";

        ConvertedTransaction converted = new ConvertedTransaction(
                foundTransaction, desiredCurrency, exchangeRate
        );

        given(service.convertForBaseCurrency(any(), any())).willReturn(converted);

        mockMvc.perform(get(CONVERSION_ENDPOINT_BASE + BASE_CURRENCY_PREFIX + "/" + desiredId + "/" + desiredCurrency)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(converted.getDescription())))
                .andExpect(jsonPath("$.date", is("2023-09-18")))
                .andExpect(jsonPath("$.amountInUSDollars", is(10.00)))
                .andExpect(jsonPath("$.targetCurrency", is(desiredCurrency)))
                .andExpect(jsonPath("$.exchangeRate", is(1.6)))
                .andExpect(jsonPath("$.convertedAmount", is(16.00)));

    }

}