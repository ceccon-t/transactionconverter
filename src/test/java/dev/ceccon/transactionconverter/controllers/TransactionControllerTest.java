package dev.ceccon.transactionconverter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.exceptions.NotFoundException;
import dev.ceccon.transactionconverter.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static dev.ceccon.transactionconverter.controllers.TransactionController.TRANSACTION_ENDPOINT_BASE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService service;

    @Test
    void testListAllTransactions() throws Exception {
        List<Transaction> inDatabase = List.of(
                new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("1.1")),
                new Transaction(2L, "Transaction2", LocalDate.now(), new BigDecimal("1.1")),
                new Transaction(3L, "Transaction3", LocalDate.now(), new BigDecimal("1.1")),
                new Transaction(4L, "Transaction4", LocalDate.now(), new BigDecimal("1.1"))
        );

        given(service.findAll()).willReturn(inDatabase);

        mockMvc.perform(get(TRANSACTION_ENDPOINT_BASE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(inDatabase.size())));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        given(service.findById(any(Long.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(TRANSACTION_ENDPOINT_BASE + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostNewValidTransaction() throws Exception {
        Transaction newTransaction = new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("1.1"));

        given(service.save(any())).willReturn(newTransaction);

        mockMvc.perform(post(TRANSACTION_ENDPOINT_BASE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isCreated());
    }

    @Test
    void testPostNewInvalidTransactionDescriptionTooLong() throws Exception {
        Transaction newTransaction = new Transaction(
                1L,
                "Transaction123456789123456789123456789123456789123456789123456789123456789",
                LocalDate.now(),
                new BigDecimal("1.1")
        );

        given(service.save(any())).willReturn(newTransaction);

        mockMvc.perform(post(TRANSACTION_ENDPOINT_BASE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostNewInvalidTransactionNegativeAmount() throws Exception {
        Transaction newTransaction = new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("-1.1"));

        given(service.save(any())).willReturn(newTransaction);

        mockMvc.perform(post(TRANSACTION_ENDPOINT_BASE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isBadRequest());
    }


}