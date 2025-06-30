package com.example.bank.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionDtoTest {

    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        transactionDto = new TransactionDto();
    }

    @Test
    void getterAndSetter_Description_ShouldWorkCorrectly() {
        String testDescription = "Grocery Shopping";
        transactionDto.setDescription(testDescription);
        assertEquals(testDescription, transactionDto.getDescription());
    }

    @Test
    void getterAndSetter_Amount_ShouldWorkCorrectly() {
        BigDecimal testAmount = new BigDecimal("25.99");
        transactionDto.setAmount(testAmount);
        assertEquals(testAmount, transactionDto.getAmount());
    }

    @Test
    void getterAndSetter_Type_ShouldWorkCorrectly() {
        String testType = "DEBIT";
        transactionDto.setType(testType);
        assertEquals(testType, transactionDto.getType());
    }

    @Test
    void getterAndSetter_Category_ShouldWorkCorrectly() {
        String testCategory = "Food";
        transactionDto.setCategory(testCategory);
        assertEquals(testCategory, transactionDto.getCategory());
    }

    @Test
    void setAmount_WithNull_ShouldSucceed() {
        transactionDto.setAmount(null);
        assertNull(transactionDto.getAmount());
    }

    @Test
    void setDescription_WithNull_ShouldSucceed() {
        transactionDto.setDescription(null);
        assertNull(transactionDto.getDescription());
    }

    @Test
    void setType_WithNull_ShouldSucceed() {
        transactionDto.setType(null);
        assertNull(transactionDto.getType());
    }

    @Test
    void setCategory_WithNull_ShouldSucceed() {
        transactionDto.setCategory(null);
        assertNull(transactionDto.getCategory());
    }

    @Test
    void allFields_WithValidValues_ShouldBeSetCorrectly() {
        TransactionDto dto = new TransactionDto();
        dto.setDescription("Salary");
        dto.setAmount(new BigDecimal("1500.00"));
        dto.setType("CREDIT");
        dto.setCategory("Income");

        assertEquals("Salary", dto.getDescription());
        assertEquals(new BigDecimal("1500.00"), dto.getAmount());
        assertEquals("CREDIT", dto.getType());
        assertEquals("Income", dto.getCategory());
    }
}