package com.example.testCase1.entities;

import com.example.testCase1.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Wallet {
    private final UUID walletId;
    private double balance;
}
