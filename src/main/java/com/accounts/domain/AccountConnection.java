package com.accounts.domain;

import java.util.List;

public record AccountConnection(List<AccountEdge> edges, AccountInfo pageInfo) {}
