package com.accounts.domain;


public record AccountInfo(String startCursor, String endCursor, Boolean hasNextPage, Boolean hasPreviousPage) {}
