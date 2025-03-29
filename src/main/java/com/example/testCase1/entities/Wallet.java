package com.example.testCase1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Wallet {
    @Id
    private  UUID walletId;
    private double balance;
    private String walletHolder;
}
