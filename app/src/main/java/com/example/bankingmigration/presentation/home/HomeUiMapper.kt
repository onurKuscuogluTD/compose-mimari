package com.example.bankingmigration.presentation.home

import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.domain.model.BankingDashboard

fun BankingDashboard.toHomeUiState(
    moneyFormatter: MoneyFormatter,
): HomeUiState {
    val totalByCurrency = accounts
        .groupBy { it.currency }
        .mapValues { (_, accounts) -> accounts.sumOf { it.balanceCents } }

    val preferredTotal = totalByCurrency["TRY"]
        ?: totalByCurrency.values.firstOrNull()
        ?: 0L

    val preferredCurrency = if (totalByCurrency.containsKey("TRY")) {
        "TRY"
    } else {
        accounts.firstOrNull()?.currency ?: "TRY"
    }

    return HomeUiState(
        isLoading = false,
        userName = user.name,
        segment = "${user.segment} musteri",
        totalBalance = moneyFormatter.format(preferredTotal, preferredCurrency),
        accountCountText = "${accounts.size} aktif hesap gorunuyor",
        suggestedActions = suggestedActions.map {
            HomeSuggestedActionUiModel(
                id = it.id,
                title = it.title,
                description = it.description,
            )
        },
    )
}
