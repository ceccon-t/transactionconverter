package dev.ceccon.transactionconverter.services;

import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.exceptions.NotFoundException;
import dev.ceccon.transactionconverter.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceJPA implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceJPA(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @Override
    public Transaction findById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }
}
