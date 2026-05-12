package com.example.bankingmigration.presentation.accounts

import com.example.bankingmigration.core.mvi.MviState

data class AccountsUiState(
    val isLoading: Boolean = true,
    val totalBalance: String = "",
    val accounts: List<AccountUiModel> = emptyList(),
    val errorMessage: String? = null,
) : MviState

data class AccountUiModel(
    val id: String,
    val name: String,
    val iban: String,
    val balance: String,
    val type: String,
)
