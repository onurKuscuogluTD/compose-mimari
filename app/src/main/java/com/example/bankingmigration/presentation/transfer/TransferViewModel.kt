package com.example.bankingmigration.presentation.transfer

import androidx.lifecycle.viewModelScope
import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.core.mvi.BaseMviViewModel
import com.example.bankingmigration.domain.model.TransferRequest
import com.example.bankingmigration.domain.usecase.GetTransferInitialDataUseCase
import com.example.bankingmigration.domain.usecase.SubmitTransferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val getTransferInitialData: GetTransferInitialDataUseCase,
    private val submitTransfer: SubmitTransferUseCase,
    private val moneyFormatter: MoneyFormatter,
) : BaseMviViewModel<TransferIntent, TransferUiState, TransferEffect>(TransferUiState()) {

    init {
        onIntent(TransferIntent.Load)
    }

    override fun onIntent(intent: TransferIntent) {
        when (intent) {
            TransferIntent.Load, TransferIntent.RetryClicked -> load()
            TransferIntent.BackClicked -> emitEffect(TransferEffect.NavigateBack)
            is TransferIntent.AccountSelected -> reduce {
                withSelectedAccount(intent.accountId)
            }
            is TransferIntent.RecipientSelected -> reduce {
                withSelectedRecipient(intent.recipientId)
            }
            TransferIntent.SubmitClicked -> submit()
        }
    }

    private fun submit() {
        val current = currentState
        val sourceAccountId = current.selectedAccountId ?: return
        val recipientId = current.selectedRecipientId ?: return

        viewModelScope.launch {
            reduce {
                copy(
                    isSubmitting = true,
                    errorMessage = null,
                    statusMessage = "Transfer talebi gonderiliyor.",
                )
            }
            runCatching {
                submitTransfer(
                    TransferRequest(
                        sourceAccountId = sourceAccountId,
                        recipientId = recipientId,
                        amountCents = DEFAULT_TRANSFER_AMOUNT_CENTS,
                    ),
                )
            }
                .onSuccess { receipt ->
                    reduce {
                        copy(
                            isSubmitting = false,
                            statusMessage = receipt.statusMessage,
                        )
                    }
                }
                .onFailure { error ->
                    reduce {
                        copy(
                            isSubmitting = false,
                            errorMessage = error.message ?: "Transfer talebi tamamlanamadi.",
                            statusMessage = "Transfer tekrar denenebilir.",
                        )
                    }
                }
        }
    }

    private fun load() {
        viewModelScope.launch {
            reduce {
                copy(
                    isLoading = true,
                    errorMessage = null,
                    statusMessage = "Transfer bilgileri hazirlaniyor.",
                )
            }
            runCatching { getTransferInitialData() }
                .onSuccess { initialData ->
                    reduce { initialData.toTransferUiState(moneyFormatter) }
                }
                .onFailure { error ->
                    reduce {
                        copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Transfer bilgileri yuklenemedi.",
                            statusMessage = "Transfer bilgileri yuklenemedi.",
                        )
                    }
                }
        }
    }

    private companion object {
        const val DEFAULT_TRANSFER_AMOUNT_CENTS = 100_000L
    }
}
