package com.transaction.bank.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Created by nitin on Thursday, January/23/2020 at 11:08 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "Bank_Account")
public class BankAccount {

    @Id
    @Column(name = "Id", nullable = false)
    private Long id;
    @Column(name = "Full_Name",nullable = false)
    private String fullName;
    @Column(name = "Balance",nullable = false)
    private double balance;

}
