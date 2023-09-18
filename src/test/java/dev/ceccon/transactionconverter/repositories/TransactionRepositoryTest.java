package dev.ceccon.transactionconverter.repositories;

import dev.ceccon.transactionconverter.entities.Transaction;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository repository;

    @Test
    void testSaveNewTransactionWithGeneratedId() {
        Transaction newTransaction = new Transaction(
                null,
                "A valid new transaction",
                LocalDate.now(),
                new BigDecimal("12.00")
        );

        Transaction persisted = repository.save(newTransaction);
        repository.flush();

        assertThat(persisted).isNotNull();
        assertThat(persisted.getId()).isNotNull();
    }

    @Test
    void testSaveEnforcesDescriptionSize() {
        assertThrows(ConstraintViolationException.class, () -> {
            Transaction newTransaction = new Transaction(
                    null,
                    "A transaction with way way waaaaaaaaaaaaaaaaaaay more than 50 chars of description",
                    LocalDate.now(),
                    new BigDecimal("12.00")
            );

            Transaction persisted = repository.save(newTransaction);
            repository.flush();
        });
    }

    @Test
    void testSaveEnforcesAmountIsNotNegative() {
        assertThrows(ConstraintViolationException.class, () -> {
            Transaction newTransaction = new Transaction(
                    null,
                    "A transaction with a negative amount",
                    LocalDate.now(),
                    new BigDecimal("-12.00")
            );

            Transaction persisted = repository.save(newTransaction);
            repository.flush();
        });
    }

}