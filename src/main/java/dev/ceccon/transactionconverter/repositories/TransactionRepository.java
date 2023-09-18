package dev.ceccon.transactionconverter.repositories;

import dev.ceccon.transactionconverter.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
