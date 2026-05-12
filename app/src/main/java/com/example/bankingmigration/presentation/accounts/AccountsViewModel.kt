package com.example.bankingmigration.presentation.accounts

import androidx.lifecycle.viewModelScope
import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.core.mvi.BaseMviViewModel
import com.example.bankingmigration.domain.usecase.GetAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getAccounts: GetAccountsUseCase,
    private val moneyFormatter: MoneyFormatter,
) : BaseMviViewModel<AccountsIntent, AccountsUiState, AccountsEffect>(AccountsUiState()) {

    init {
        onIntent(AccountsIntent.Load)
    }

    override fun onIntent(intent: AccountsIntent) {
        when (intent) {
            AccountsIntent.Load, AccountsIntent.RetryClicked -> loadAccounts()
            AccountsIntent.BackClicked -> emitEffect(AccountsEffect.NavigateBack)
            AccountsIntent.TransferClicked -> emitEffect(AccountsEffect.NavigateToTransfer)
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            reduce { copy(isLoading = true, errorMessage = null) }
            runCatching { getAccounts() }
                .onSuccess { accounts ->
                    reduce { accounts.toAccountsUiState(moneyFormatter) }
                }
                .onFailure { error ->
                    reduce {
                        copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Hesaplar yuklenemedi.",
                        )
                    }
                }
        }
    }
}
