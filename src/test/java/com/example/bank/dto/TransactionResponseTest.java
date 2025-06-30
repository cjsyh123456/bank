package com.example.bank.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionResponseTest {

    private TransactionResponse transactionResponse;
    private UUID testId;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testTimestamp = LocalDateTime.now();

        transactionResponse = new TransactionResponse();
        transactionResponse.setId(testId);
        transactionResponse.setDescription("Salary Deposit");
        transactionResponse.setAmount(new BigDecimal("1500.00"));
        transactionResponse.setType("CREDIT");
        transactionResponse.setTimestamp(testTimestamp);
        transactionResponse.setCategory("Salary");
    }

    @Test
    void getterAndSetter_Id_ShouldWorkCorrectly() {
        UUID newId = UUID.randomUUID();
        transactionResponse.setId(newId);
        assertEquals(newId, transactionResponse.getId());
    }

    @Test
    void getterAndSetter_Description_ShouldWorkCorrectly() {
        String newDescription = "Bonus Payment";
        transactionResponse.setDescription(newDescription);
        assertEquals(newDescription, transactionResponse.getDescription());
    }

    @Test
    void getterAndSetter_Amount_ShouldWorkCorrectly() {
        BigDecimal newAmount = new BigDecimal("2000.00");
        transactionResponse.setAmount(newAmount);
        assertEquals(newAmount, transactionResponse.getAmount());
    }

    @Test
    void getterAndSetter_Type_ShouldWorkCorrectly() {
        String newType = "DEBIT";
        transactionResponse.setType(newType);
        assertEquals(newType, transactionResponse.getType());
    }

    @Test
    void getterAndSetter_Timestamp_ShouldWorkCorrectly() {
        LocalDateTime newTimestamp = LocalDateTime.now().plusDays(1);
        transactionResponse.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, transactionResponse.getTimestamp());
    }

    @Test
    void getterAndSetter_Category_ShouldWorkCorrectly() {
        String newCategory = "Bonus";
        transactionResponse.setCategory(newCategory);
        assertEquals(newCategory, transactionResponse.getCategory());
    }

    @Test
    void setAmount_WithNull_ShouldSucceed() {
        transactionResponse.setAmount(null);
        assertNull(transactionResponse.getAmount());
    }

    @Test
    void setDescription_WithNull_ShouldSucceed() {
        transactionResponse.setDescription(null);
        assertNull(transactionResponse.getDescription());
    }

    @Test
    void setType_WithNull_ShouldSucceed() {
        transactionResponse.setType(null);
        assertNull(transactionResponse.getType());
    }

    @Test
    void setCategory_WithNull_ShouldSucceed() {
        transactionResponse.setCategory(null);
        assertNull(transactionResponse.getCategory());
    }

    @Test
    void setTimestamp_WithNull_ShouldSucceed() {
        transactionResponse.setTimestamp(null);
        assertNull(transactionResponse.getTimestamp());
    }

    @Test
    void allFields_WithValidValues_ShouldBeSetCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        TransactionResponse response = new TransactionResponse();
        response.setId(id);
        response.setDescription("Rent");
        response.setAmount(new BigDecimal("1000.00"));
        response.setType("DEBIT");
        response.setTimestamp(timestamp);
        response.setCategory("Housing");

        assertEquals(id, response.getId());
        assertEquals("Rent", response.getDescription());
        assertEquals(new BigDecimal("1000.00"), response.getAmount());
        assertEquals("DEBIT", response.getType());
        assertEquals(timestamp, response.getTimestamp());
        assertEquals("Housing", response.getCategory());
    }
}