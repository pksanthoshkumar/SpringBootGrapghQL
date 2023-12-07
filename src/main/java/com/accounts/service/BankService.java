package com.accounts.service;

import com.accounts.domain.*;
import com.accounts.entity.BankAccountResult;
import com.accounts.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankService {

    private final AccountRepository accontsRepository;
    public AccountConnection getAccountConnection(CursorInfo cursorInfo) {
        int pageSize = cursorInfo.pageSize();

        // Limit is pageSize + 1
        var limit = pageSize + 1;
        List<BankAccountResult> accountResults;
        boolean hasNextPage;
        boolean hasPreviousPage;
        String startCursor;
        String endCursor;

        if (cursorInfo.hasCursors() && cursorInfo.hasNextPageCursor()) {
            accountResults =
                    accontsRepository.accountsWithNextPageCursor(CursorInfo.decode(cursorInfo.getCursor()), limit);
            int resultSize = accountResults.size();
            var firstResult = accountResults.get(0);
            hasPreviousPage = firstResult.hasPage();
            startCursor = CursorInfo.encode(firstResult.id());
            int endCursorIndex = resultSize > pageSize ? pageSize - 1 : resultSize - 1;
            endCursor = CursorInfo.encode(accountResults.get(endCursorIndex).id());
            hasNextPage = resultSize > pageSize;

        } else if (cursorInfo.hasCursors() && cursorInfo.hasPrevPageCursor()) {
            accountResults =
                    accontsRepository.accountsWithPreviousPageCursor(
                            CursorInfo.decode(cursorInfo.getCursor()), limit);
            int resultSize = accountResults.size();
            var firstResult = accountResults.get(0);
            hasNextPage = firstResult.hasPage();
            startCursor = CursorInfo.encode(firstResult.id());
            int endCursorIndex = resultSize > pageSize ? pageSize - 1 : resultSize - 1;
            endCursor = CursorInfo.encode(accountResults.get(endCursorIndex).id());
            hasPreviousPage = resultSize > pageSize;
        } else {
            accountResults = accontsRepository.accountsWithoutCursor(limit);
            int resultSize = accountResults.size();
            hasPreviousPage = false;
            var firstResult = accountResults.get(0);
            startCursor = CursorInfo.encode(firstResult.id());
            int endCursorIndex = resultSize > pageSize ? pageSize - 1 : resultSize - 1;
            endCursor = CursorInfo.encode(accountResults.get(endCursorIndex).id());
            hasNextPage = resultSize > pageSize;
        }

        // TODO check what results are returned when requested out of range
        if (accountResults.size() == 0) {
            return new AccountConnection(null, new AccountInfo(null, null, false, false));
        }

        var accountEdges =
                accountResults.stream()
                        .limit(cursorInfo.pageSize())
                        .map(acountResult ->
                                        new AccountEdge(
                                                CursorInfo.encode(acountResult.id()),
                                                new BankAccount(
                                                        acountResult.id(),
                                                        acountResult.balance(),
                                                        acountResult.status(),
                                                        acountResult.transferLimit(),
                                                        acountResult.accountCreateDate()))).toList();
        var accountConnection =
                new AccountConnection(
                        accountEdges, new AccountInfo(startCursor, endCursor, hasNextPage, hasPreviousPage));
        log.info("Pages, {}", accountConnection);
        return accountConnection;
    }
}