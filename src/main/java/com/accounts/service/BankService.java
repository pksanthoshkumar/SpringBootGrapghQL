package com.accounts.service;

import com.accounts.domain.BankAccount;
import com.accounts.exceptions.AccountNotFountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class BankService {

    private static SortedSet<BankAccount> bankAccounts = new TreeSet<>(Comparator.comparing(BankAccount::getId));

    public SortedSet<BankAccount> getAccounts() {
        return bankAccounts;
    }

    public BankAccount save(BankAccount account) {
        bankAccounts.add(account);
        return account;
    }

    public BankAccount getAccounts(Integer accountId) {
        Optional <BankAccount> account = bankAccounts.stream().filter(a -> a.getId().equals(accountId)).findFirst();
        if (account.isPresent())
            return account.get();
        else {
            throw new AccountNotFountException("Account Not found");
        }
    }
}