package com.example.bankingmigration.presentation.accounts

import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.domain.model.BankAccount

fun List<BankAccount>.toAccountsUiState(
    moneyFormatter: MoneyFormatter,
): AccountsUiState {
    val tryTotal = filter { it.currency == "TRY" }.sumOf { it.balanceCents }
    val totalCurrency = if (tryTotal > 0) "TRY" else firstOrNull()?.currency ?: "TRY"
    val total = if (tryTotal > 0) tryTotal else firstOrNull()?.balanceCents ?: 0L

    return AccountsUiState(
        isLoading = false,
        totalBalance = moneyFormatter.format(total, totalCurrency),
        accounts = map { account ->
            AccountUiModel(
                id = account.id,
                name = account.name,
                iban = account.iban,
                balance = moneyFormatter.format(account.balanceCents, account.currency),
                type = account.type,
            )
        },
    )
}
