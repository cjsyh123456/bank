package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Transaction data transfer object")
public class TransactionDto {

    @Schema(description = "Description of the transaction", example = "Fruit from Hema", required = true)
    private String description;

    @Schema(description = "Amount of the transaction", example = "23.18", required = true)
    private BigDecimal amount;

    @Schema(description = "Type of transaction (CREDIT/DEBIT)", example = "DEBIT", required = true)
    private String type;

    @Schema(description = "Category of the transaction", example = "Food", required = false)
    private String category;

    // Getters and Setters remain the same
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}