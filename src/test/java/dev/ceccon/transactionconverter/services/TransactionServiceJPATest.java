package dev.ceccon.transactionconverter.services;

import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.exceptions.NotFoundException;
import dev.ceccon.transactionconverter.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransactionServiceJPATest {

    @InjectMocks
    TransactionServiceJPA service;

    @Mock
    TransactionRepository repository;

    @Test
    void testSaveReturnsPersistedEntity() {
        Transaction newTransaction = new Transaction(null, "Transaction1", LocalDate.now(), new BigDecimal("1.1"));
        Transaction persistedTransaction = new Transaction(1L, newTransaction.getDescription(), newTransaction.getDate(), newTransaction.getAmountInUSDollars());

        given(repository.save(any(Transaction.class))).willReturn(persistedTransaction);

        Transaction returned = service.save(newTransaction);

        assertThat(returned).isNotNull();
        assertThat(returned).isEqualTo(persistedTransaction);
    }

    @Test
    void testFindAll() {
        List<Transaction> inDatabase = List.of(
                new Transaction(1L, "Transaction1", LocalDate.now(), new BigDecimal("1.1")),
                new Transaction(2L, "Transaction2", LocalDate.now(), new BigDecimal("1.1")),
                new Transaction(3L, "Transaction3", LocalDate.now(), new BigDecimal("1.1")),
                new Transaction(4L, "Transaction4", LocalDate.now(), new BigDecimal("1.1"))
        );

        given(repository.findAll()).willReturn(inDatabase);

        List<Transaction> all = service.findAll();

        assertThat(all.size()).isEqualTo(inDatabase.size());
    }

    @Test
    void testFindByIdReturnsEntityFound() {
        Long desiredId = 1L;
        Transaction persistedTransaction = new Transaction(desiredId, "Transaction1", LocalDate.now(), new BigDecimal("1.1"));

        given(repository.findById(desiredId)).willReturn(Optional.of(persistedTransaction));

        Transaction found = service.findById(desiredId);

        assertThat(found).isEqualTo(persistedTransaction);
    }

    @Test
    void testFindByIdThrowsCustomExceptionWhenNotFound() {
        assertThrows(NotFoundException.class, () -> {
            Long desiredId = 1L;

            given(repository.findById(desiredId)).willReturn(Optional.empty());

            Transaction found = service.findById(desiredId);
        });
    }

}