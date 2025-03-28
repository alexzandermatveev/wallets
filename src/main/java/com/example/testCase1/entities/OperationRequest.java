package com.example.testCase1.entities;

import com.example.testCase1.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.util.UUID;

@RequiredArgsConstructor
@Data
public class OperationRequest {

    @NotNull(message = "Wallet ID must not be null")
    private final UUID walletId;
    @NotNull(message = "Operation type must not be null")
    private final OperationType operationType;
    @NotNull(message = "Amount must not be null")
    private final double amount;
}
