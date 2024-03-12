package com.transaction.bank.service;


import com.transaction.bank.entity.BankAccount;
import com.transaction.bank.exception.BankAccountNotFoundException;
import com.transaction.bank.repository.BankAccountDao;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class BankAccountService  {
    private BankAccountDao bankAccountDao;

    public List<BankAccount> listBankAccountInfo() {
        return bankAccountDao.findAll();
    }

    public BankAccount findAccountNumber(Long id) {
        log.info("Inside BankAccount findById(Long id)");
        Optional<BankAccount> bankAccountOptional = bankAccountDao.findById(id);
        if (bankAccountOptional.isEmpty()){
            throw new BankAccountNotFoundException("Account No : " + id);
        }

        return bankAccountOptional.get();
    }
}
