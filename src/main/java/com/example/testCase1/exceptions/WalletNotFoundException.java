package com.example.testCase1.exceptions;

public class WalletNotFoundException extends RuntimeException  {
    public WalletNotFoundException(String message){
        super(message);
    }
}
