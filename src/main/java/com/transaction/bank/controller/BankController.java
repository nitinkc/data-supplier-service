package com.transaction.bank.controller;

import java.util.List;
import java.util.Map;

import com.transaction.bank.dto.ReceivedMoneyDto;
import com.transaction.bank.entity.BankAccount;
import com.transaction.bank.exception.BankTransactionException;
import com.transaction.bank.service.BankAccountService;
import com.transaction.bank.service.BankTransactionService;
import com.transaction.bank.service.MoneyTransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by nitin on Thursday, January/23/2020 at 11:36 PM
 */
@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class BankController {
    private BankAccountService bankAccountService;
    private BankTransactionService bankTransactionService;
    private MoneyTransferService moneyTransferService;

    @GetMapping("")
    public String HelloWorld() {
        String str = "Hello!! The Banking System is UP and Running";
        log.info(str);
        return str;
    }

    @GetMapping(value = "/allAccounts")
    public List<BankAccount> showBankAccounts(Model model) {
        List<BankAccount> list = bankAccountService.listBankAccountInfo();
        log.info("Bank Accounts " + list.toString() + " in a seat...");
        model.addAttribute("accountInfos", list);
        return list;
    }

    @PostMapping(value = "/sendMoney")
    public List<BankAccount> viewSendMoneyPage(@RequestBody Map<String, Object> requestBody) {
        Long fromAccount = ((Integer) requestBody.get("fromAccount")).longValue();
        Long toAccount = ((Integer) requestBody.get("toAccount")).longValue();
        double amount = (Double) requestBody.get("amount");

        log.info("Send Money From: {} to: {} Amount: {}", fromAccount, toAccount, amount);
        try {
            bankTransactionService.sendMoney(fromAccount, toAccount, amount);
        } catch (BankTransactionException e) {
            log.error(e.getMessage());
        }
        return bankAccountService.listBankAccountInfo();
    }


    @PostMapping("/receiveMoney")
    public ResponseEntity<ReceivedMoneyDto> receiveMoney(@RequestBody Map<String, Object> requestBody) {
        try {
            // Process the original action
            ReceivedMoneyDto receivedMoneyDto = moneyTransferService.receiveMoney(requestBody);
            return ResponseEntity.ok(receivedMoneyDto);
        } catch (Exception e) {
            // If an exception occurs, initiate rollback
            moneyTransferService.rollbackMoney(requestBody);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ReceivedMoneyDto.builder()
                        .message("Error processing request")
                        .build());
        }
    }

    @PostMapping("/rollbackMoney")
    public ResponseEntity<String> rollbackMoney(@RequestBody Map<String, Object> requestBody) {
        try {
            // Process the rollback action
            moneyTransferService.rollbackMoney(requestBody);
            return ResponseEntity.ok("Rollback successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rolling back transaction");
        }
    }
}