package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Transaction response object")
public class TransactionResponse {

    @Schema(description = "Unique identifier of the transaction", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Description of the transaction", example = "Salary deposit")
    private String description;

    @Schema(description = "Amount of the transaction", example = "1500.00")
    private BigDecimal amount;

    @Schema(description = "Type of transaction (CREDIT/DEBIT)", example = "CREDIT")
    private String type;

    @Schema(description = "Timestamp of the transaction", example = "2023-10-15T12:34:56.789")
    private LocalDateTime timestamp;

    @Schema(description = "Category of the transaction", example = "Salary")
    private String category;

    // Getters and Setters remain the same
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}