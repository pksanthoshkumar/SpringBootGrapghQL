package com.accounts.repo;

import com.accounts.entity.BankAccountEntity;
import com.accounts.entity.BankAccountResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<BankAccountEntity, Integer> {

    @Query(nativeQuery = true)
    List<BankAccountResult> accountsWithNextPageCursor(Integer id, int limit);

    @Query(nativeQuery = true)
    List<BankAccountResult> accountsWithPreviousPageCursor(Integer id, int limit);

    @Query(nativeQuery = true)
    List<BankAccountResult> accountsWithoutCursor(int limit);
}
