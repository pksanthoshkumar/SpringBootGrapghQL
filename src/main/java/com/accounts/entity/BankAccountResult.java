package com.accounts.entity;

public record BankAccountResult (
    Integer id, 
    Float balance, 
    String status, 
    Float transferLimit, 
    String accountCreateDate, 
    Boolean hasPage
    ){}
