package dev.ceccon.transactionconverter.controllers;

import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class TransactionController {

    public static final String TRANSACTION_ENDPOINT_BASE = "/api/v1/transaction";

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping(TRANSACTION_ENDPOINT_BASE)
    public List<Transaction> handleGet() {
        return service.findAll();
    }

    @PostMapping(TRANSACTION_ENDPOINT_BASE)
    public ResponseEntity<Transaction> handlePost(@Valid @RequestBody Transaction newTransaction) {
        newTransaction.setId(null);
        Transaction saved = service.save(newTransaction);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}
