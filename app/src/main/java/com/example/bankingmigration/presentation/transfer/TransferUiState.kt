package com.example.bankingmigration.presentation.transfer

data class TransferUiState(
    val isLoading: Boolean = true,
    val accounts: List<TransferAccountUiModel> = emptyList(),
    val recipients: List<TransferRecipientUiModel> = emptyList(),
    val selectedAccountId: String? = null,
    val selectedRecipientId: String? = null,
    val amountText: String = "1.000,00 TL",
    val statusMessage: String = "Transfer bilgileri hazirlaniyor.",
    val errorMessage: String? = null,
    val isSubmitting: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !isLoading &&
            !isSubmitting &&
            errorMessage == null &&
            selectedAccountId != null &&
            selectedRecipientId != null
}

data class TransferAccountUiModel(
    val id: String,
    val name: String,
    val balance: String,
    val type: String,
    val isSelected: Boolean,
)

data class TransferRecipientUiModel(
    val id: String,
    val displayName: String,
    val bankName: String,
    val maskedIban: String,
    val isSelected: Boolean,
)
