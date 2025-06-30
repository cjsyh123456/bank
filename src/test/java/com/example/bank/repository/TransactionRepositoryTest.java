package com.example.bank.repository;

import com.example.bank.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;
    private Transaction sampleTransaction;
    private UUID sampleTransactionId;

    @BeforeEach
    void setUp() {
        transactionRepository = new TransactionRepository();
        sampleTransactionId = UUID.randomUUID();

        sampleTransaction = new Transaction();
        sampleTransaction.setId(sampleTransactionId);
        sampleTransaction.setDescription("Salary Deposit");
        sampleTransaction.setAmount(new BigDecimal("1500.00"));
        sampleTransaction.setType("CREDIT");
        sampleTransaction.setCategory("Salary");
    }

    @Test
    void save_ShouldStoreTransactionAndReturnIt() {
        // Act
        Transaction savedTransaction = transactionRepository.save(sampleTransaction);

        // Assert
        assertNotNull(savedTransaction);
        assertEquals(sampleTransactionId, savedTransaction.getId());
        assertTrue(transactionRepository.existsById(sampleTransactionId));
    }

    @Test
    void findById_WithExistingId_ShouldReturnTransaction() {
        // Arrange
        transactionRepository.save(sampleTransaction);

        // Act
        Optional<Transaction> foundTransaction = transactionRepository.findById(sampleTransactionId);

        // Assert
        assertTrue(foundTransaction.isPresent());
        assertEquals(sampleTransactionId, foundTransaction.get().getId());
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Act
        Optional<Transaction> foundTransaction = transactionRepository.findById(nonExistingId);

        // Assert
        assertFalse(foundTransaction.isPresent());
    }

    @Test
    void findAll_WithNoTransactions_ShouldReturnEmptyList() {
        // Act
        List<Transaction> transactions = transactionRepository.findAll();

        // Assert
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    void findAll_WithMultipleTransactions_ShouldReturnAllTransactions() {
        // Arrange
        Transaction transaction1 = new Transaction("Salary", new BigDecimal("2000.00"), "CREDIT", "Income");
        Transaction transaction2 = new Transaction("Rent", new BigDecimal("1000.00"), "DEBIT", "Housing");

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        // Act
        List<Transaction> transactions = transactionRepository.findAll();

        // Assert
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    @Test
    void deleteById_WithExistingId_ShouldRemoveTransaction() {
        // Arrange
        transactionRepository.save(sampleTransaction);
        assertTrue(transactionRepository.existsById(sampleTransactionId));

        // Act
        transactionRepository.deleteById(sampleTransactionId);

        // Assert
        assertFalse(transactionRepository.existsById(sampleTransactionId));
        assertFalse(transactionRepository.findById(sampleTransactionId).isPresent());
    }

    @Test
    void deleteById_WithNonExistingId_ShouldDoNothing() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Act & Assert (should not throw exception)
        assertDoesNotThrow(() -> transactionRepository.deleteById(nonExistingId));
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        // Arrange
        transactionRepository.save(sampleTransaction);

        // Act & Assert
        assertTrue(transactionRepository.existsById(sampleTransactionId));
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Act & Assert
        assertFalse(transactionRepository.existsById(nonExistingId));
    }

    @Test
    void save_ShouldHandleConcurrentAccess() throws InterruptedException {
        // Arrange
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                Transaction t = new Transaction();
                t.setDescription("Transaction " + index);
                transactionRepository.save(t);
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        assertEquals(threadCount, transactionRepository.findAll().size());
    }
}