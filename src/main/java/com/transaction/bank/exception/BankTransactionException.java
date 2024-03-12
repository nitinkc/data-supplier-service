package com.transaction.bank.exception;

public class BankTransactionException extends RuntimeException {
    public BankTransactionException(String message){
        super(message);
    }
}
