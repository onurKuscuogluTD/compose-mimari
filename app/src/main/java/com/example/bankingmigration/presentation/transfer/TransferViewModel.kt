package com.example.bankingmigration.presentation.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.domain.model.TransferRequest
import com.example.bankingmigration.domain.usecase.GetTransferInitialDataUseCase
import com.example.bankingmigration.domain.usecase.SubmitTransferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val getTransferInitialData: GetTransferInitialDataUseCase,
    private val submitTransfer: SubmitTransferUseCase,
    private val moneyFormatter: MoneyFormatter,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() {
        load()
    }

    fun selectAccount(accountId: String) {
        _uiState.update { current ->
            current.withSelectedAccount(accountId)
        }
    }

    fun selectRecipient(recipientId: String) {
        _uiState.update { current ->
            current.withSelectedRecipient(recipientId)
        }
    }

    fun submit() {
        val current = _uiState.value
        val sourceAccountId = current.selectedAccountId ?: return
        val recipientId = current.selectedRecipientId ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
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
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            statusMessage = receipt.statusMessage,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
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
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    statusMessage = "Transfer bilgileri hazirlaniyor.",
                )
            }
            runCatching { getTransferInitialData() }
                .onSuccess { initialData ->
                    _uiState.value = initialData.toTransferUiState(moneyFormatter)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
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
