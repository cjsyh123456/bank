package com.example.bank.repository;

import com.example.bank.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {
    private static final Logger logger = LoggerFactory.getLogger(TransactionRepository.class);
    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();

    public Transaction save(Transaction transaction) {
        logger.debug("Attempting to save transaction with ID: {}", transaction.getId());
        transactions.put(transaction.getId(), transaction);
        logger.info("Transaction saved successfully with ID: {}", transaction.getId());
        return transaction;
    }

    public Optional<Transaction> findById(UUID id) {
        logger.debug("Looking up transaction by ID: {}", id);
        Optional<Transaction> result = Optional.ofNullable(transactions.get(id));
        if (result.isPresent()) {
            logger.debug("Found transaction with ID: {}", id);
        } else {
            logger.debug("No transaction found with ID: {}", id);
        }
        return result;
    }

    public List<Transaction> findAll() {
        logger.debug("Retrieving all transactions");
        List<Transaction> result = new ArrayList<>(transactions.values());
        logger.info("Returning {} transactions", result.size());
        return result;
    }

    public void deleteById(UUID id) {
        logger.debug("Attempting to delete transaction with ID: {}", id);
        if (transactions.containsKey(id)) {
            transactions.remove(id);
            logger.info("Transaction deleted successfully with ID: {}", id);
        } else {
            logger.warn("Attempted to delete non-existent transaction with ID: {}", id);
        }
    }

    public boolean existsById(UUID id) {
        logger.debug("Checking existence of transaction with ID: {}", id);
        boolean exists = transactions.containsKey(id);
        logger.debug("Transaction with ID {} {} exists", id, exists ? "does" : "does not");
        return exists;
    }
}