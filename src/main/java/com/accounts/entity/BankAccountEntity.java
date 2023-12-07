package com.accounts.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NamedNativeQuery(
        name = "BankAccountEntity.accountsWithNextPageCursor",
        query =
                """
                    SELECT id,balance, status, transfer_limit, account_create_date,
                    EXISTS (SELECT 1 FROM bank_account WHERE id < ?1) AS has_page
                    FROM bank_account
                    WHERE id > ?1
                    ORDER BY id
                    LIMIT ?2
                    """,
        resultSetMapping = "Mapping.BankAccountResult")
@NamedNativeQuery(
        name = "BankAccountEntity.accountsWithPreviousPageCursor",
        query =
                """
                    SELECT id,balance, status, transfer_limit, account_create_date,
                    EXISTS (SELECT 1 FROM bank_account WHERE id > ?1) AS has_page
                    FROM bank_account
                    WHERE id < ?1
                    ORDER BY id
                    LIMIT ?2
                """,
        resultSetMapping = "Mapping.BankAccountResult")
@NamedNativeQuery(
        name = "BankAccountEntity.accountsWithoutCursor",
        query =
                """
                    SELECT id,balance, status, transfer_limit, account_create_date,
                    false AS has_page
                    FROM bank_account
                    ORDER BY ID LIMIT ?1
                 """,
        resultSetMapping = "Mapping.BankAccountResult")
@SqlResultSetMapping(
        name = "Mapping.BankAccountResult",
        classes =
        @ConstructorResult(
                targetClass = BankAccountResult.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "balance"),
                        @ColumnResult(name = "status"),
                        @ColumnResult(name = "transfer_limit"),
                        @ColumnResult(name = "account_create_date"),
                        @ColumnResult(name = "has_page")
                }))
@Entity
@Data
@NoArgsConstructor
@Table(name = "bank_account")
public class BankAccountEntity {
    @Id
    private Integer id;
    private Float balance;
    private String status;
    private Float transferLimit;
    private String accountCreateDate;
}
