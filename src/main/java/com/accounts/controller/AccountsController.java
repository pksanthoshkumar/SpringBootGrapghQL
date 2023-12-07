package com.accounts.controller;

import com.accounts.domain.AccountConnection;
import com.accounts.domain.CursorInfo;
import com.accounts.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AccountsController {

    private final BankService bankService;

    @QueryMapping()
    public AccountConnection accountsByPage(
            @Argument("first") Integer first,
            @Argument("after") String after,
            @Argument("last") Integer last,
            @Argument("before") String before) {
        return bankService.getAccountConnection(new CursorInfo(first, after, last, before));
    }
}

