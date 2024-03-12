package com.transaction.bank.dto;

import com.transaction.bank.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReceivedMoneyDto {
    private String message;
    private BankAccount bankAccount;
}
