package dev.ceccon.transactionconverter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static dev.ceccon.transactionconverter.controllers.TransactionController.TRANSACTION_ENDPOINT_BASE;

@SpringBootTest
@ActiveProfiles("test")
class UseCase01Test_PersistNewTransaction {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @Test
    void persistNewTransaction() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Transaction transaction = new Transaction(null, "NewTransaction", LocalDate.now(), new BigDecimal("10.20"));

        mockMvc.perform(post(TRANSACTION_ENDPOINT_BASE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andReturn();

        assertThat(transactionRepository.findAll()
                            .stream()
                            .anyMatch(t -> t.getDescription().equals(transaction.getDescription())))
                .isEqualTo(true);
    }

}