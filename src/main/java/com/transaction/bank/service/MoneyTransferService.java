package com.transaction.bank.service;


import com.transaction.bank.dto.ReceivedMoneyDto;
import com.transaction.bank.entity.BankAccount;
import com.transaction.bank.exception.BankTransactionException;
import com.transaction.bank.repository.BankAccountDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * Created by nitin on Friday, January/31/2020 at 11:36 PM
 */
@Service
@Slf4j
@AllArgsConstructor
public class MoneyTransferService {

    private BankAccountService bankAccountService;
    private BankAccountDao bankAccountDao;

    public void addAmount(Long id, double amount) throws BankTransactionException {
        log.info("Add Amount Started");
        BankAccount account = bankAccountService.findAccountNumber(id);

        if (account == null) {
            log.error("bankAccountService.findById(id) returns null for id : " + id);
            throw new BankTransactionException("Account not found " + id);
        }
        double newBalance = account.getBalance() + amount;

        if (account.getBalance() + amount < 0) {
            throw new BankTransactionException(
                    "The money in the account '" + id + "' is not enough (" + account.getBalance() + ")");
        }

        account.setBalance(newBalance);
        log.info("Add Amount Ended");

        // Explicit Save is not required when using Tx
        //bankAccountDao.save(account);
    }

    public void removeAmount(Long fromAccountId, double amount) {
        addAmount(fromAccountId,-amount);
    }

    public ReceivedMoneyDto receiveMoney(Map<String, Object> requestBody) {
        log.info("Add Receiving Started");

        long toAccount = ((Integer) requestBody.get("toAccount")).longValue();
        BankAccount account = bankAccountService.findAccountNumber(toAccount);
        double oldBalance = account.getBalance();

        account.setBalance(oldBalance + (Double) requestBody.get("amount"));
        bankAccountDao.save(account);
        log.info("Add Amount Ended");

        return ReceivedMoneyDto.builder()
                .bankAccount(account)
                .message("Amount before Transferring the Money in:: " + account.getId() + " is :: " +oldBalance)
                .build();
    }

    public void rollbackMoney(Map<String, Object> requestBody) {

    }
}
