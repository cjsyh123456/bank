package com.example.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;
    private UUID testId;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testTimestamp = LocalDateTime.now();

        transaction = new Transaction();
        transaction.setId(testId);
        transaction.setDescription("Salary Deposit");
        transaction.setAmount(new BigDecimal("1500.00"));
        transaction.setType("CREDIT");
        transaction.setTimestamp(testTimestamp);
        transaction.setCategory("Salary");
    }

    @Test
    void noArgConstructor_ShouldInitializeIdAndTimestamp() {
        Transaction newTransaction = new Transaction();

        assertNotNull(newTransaction.getId());
        assertNotNull(newTransaction.getTimestamp());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(newTransaction.getTimestamp().isBefore(now) ||
                newTransaction.getTimestamp().isEqual(now));
    }

    @Test
    void allArgConstructor_ShouldSetAllFields() {
        Transaction newTransaction = new Transaction(
                "Grocery",
                new BigDecimal("100.50"),
                "DEBIT",
                "Shopping"
        );

        assertNotNull(newTransaction.getId());
        assertNotNull(newTransaction.getTimestamp());
        assertEquals("Grocery", newTransaction.getDescription());
        assertEquals(new BigDecimal("100.50"), newTransaction.getAmount());
        assertEquals("DEBIT", newTransaction.getType());
        assertEquals("Shopping", newTransaction.getCategory());
    }

    @Test
    void getId_ShouldReturnCorrectId() {
        assertEquals(testId, transaction.getId());
    }

    @Test
    void getDescription_ShouldReturnCorrectDescription() {
        assertEquals("Salary Deposit", transaction.getDescription());
    }

    @Test
    void getAmount_ShouldReturnCorrectAmount() {
        assertEquals(new BigDecimal("1500.00"), transaction.getAmount());
    }

    @Test
    void getType_ShouldReturnCorrectType() {
        assertEquals("CREDIT", transaction.getType());
    }

    @Test
    void getTimestamp_ShouldReturnCorrectTimestamp() {
        assertEquals(testTimestamp, transaction.getTimestamp());
    }

    @Test
    void getCategory_ShouldReturnCorrectCategory() {
        assertEquals("Salary", transaction.getCategory());
    }

    @Test
    void setId_ShouldUpdateId() {
        UUID newId = UUID.randomUUID();
        transaction.setId(newId);
        assertEquals(newId, transaction.getId());
    }

    @Test
    void setDescription_ShouldUpdateDescription() {
        transaction.setDescription("Bonus Payment");
        assertEquals("Bonus Payment", transaction.getDescription());
    }

    @Test
    void setAmount_ShouldUpdateAmount() {
        transaction.setAmount(new BigDecimal("2000.00"));
        assertEquals(new BigDecimal("2000.00"), transaction.getAmount());
    }

    @Test
    void setType_ShouldUpdateType() {
        transaction.setType("DEBIT");
        assertEquals("DEBIT", transaction.getType());
    }

    @Test
    void setTimestamp_ShouldUpdateTimestamp() {
        LocalDateTime newTimestamp = LocalDateTime.now().plusDays(1);
        transaction.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, transaction.getTimestamp());
    }

    @Test
    void setCategory_ShouldUpdateCategory() {
        transaction.setCategory("Bonus");
        assertEquals("Bonus", transaction.getCategory());
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        Transaction sameTransaction = new Transaction();
        sameTransaction.setId(testId);

        assertTrue(transaction.equals(sameTransaction));
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        Transaction differentTransaction = new Transaction();
        differentTransaction.setId(UUID.randomUUID());

        assertFalse(transaction.equals(differentTransaction));
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        assertFalse(transaction.equals(null));
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        assertFalse(transaction.equals("Not a Transaction"));
    }

    @Test
    void hashCode_WithSameId_ShouldBeEqual() {
        Transaction sameTransaction = new Transaction();
        sameTransaction.setId(testId);

        assertEquals(transaction.hashCode(), sameTransaction.hashCode());
    }

    @Test
    void hashCode_WithDifferentId_ShouldNotBeEqual() {
        Transaction differentTransaction = new Transaction();
        differentTransaction.setId(UUID.randomUUID());

        assertNotEquals(transaction.hashCode(), differentTransaction.hashCode());
    }

    @Test
    void setAmount_WithNull_ShouldSucceed() {
        transaction.setAmount(null);
        assertNull(transaction.getAmount());
    }

    @Test
    void setDescription_WithNull_ShouldSucceed() {
        transaction.setDescription(null);
        assertNull(transaction.getDescription());
    }

    @Test
    void setType_WithNull_ShouldSucceed() {
        transaction.setType(null);
        assertNull(transaction.getType());
    }

    @Test
    void setCategory_WithNull_ShouldSucceed() {
        transaction.setCategory(null);
        assertNull(transaction.getCategory());
    }

    @Test
    void setTimestamp_WithNull_ShouldSucceed() {
        transaction.setTimestamp(null);
        assertNull(transaction.getTimestamp());
    }
}