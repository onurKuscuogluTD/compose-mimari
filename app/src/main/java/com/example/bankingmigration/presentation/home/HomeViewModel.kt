package com.example.bankingmigration.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.core.mvi.BaseMviViewModel
import com.example.bankingmigration.domain.usecase.GetHomeSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSummary: GetHomeSummaryUseCase,
    private val moneyFormatter: MoneyFormatter,
) : BaseMviViewModel<HomeIntent, HomeUiState, HomeEffect>(HomeUiState()) {

    init {
        onIntent(HomeIntent.Load)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load, HomeIntent.RetryClicked -> load()
            HomeIntent.AccountsClicked -> emitEffect(HomeEffect.NavigateToAccounts)
            HomeIntent.TransferClicked -> emitEffect(HomeEffect.NavigateToTransfer)
        }
    }

    private fun load() {
        viewModelScope.launch {
            reduce { copy(isLoading = true, errorMessage = null) }
            runCatching { getHomeSummary() }
                .onSuccess { dashboard ->
                    reduce { dashboard.toHomeUiState(moneyFormatter) }
                }
                .onFailure { error ->
                    reduce {
                        copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Ana sayfa verileri yuklenemedi.",
                        )
                    }
                }
        }
    }
}
