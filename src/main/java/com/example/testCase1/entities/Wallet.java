package com.example.testCase1.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@NoArgsConstructor
@Data
public class Wallet {
    @Id
    private  UUID walletId;
    private double balance;
    private String walletHolder;
}
