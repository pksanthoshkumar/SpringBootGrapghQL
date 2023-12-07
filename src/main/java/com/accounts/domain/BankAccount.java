package com.accounts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record  BankAccount (
    Integer id,
    Float balance,
    String status,
    Float transferLimit,
    String accountCreateDate
    ) {}
