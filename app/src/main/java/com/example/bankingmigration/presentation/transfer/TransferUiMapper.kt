package com.example.bankingmigration.presentation.transfer

import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.domain.usecase.TransferInitialData

fun TransferInitialData.toTransferUiState(
    moneyFormatter: MoneyFormatter,
): TransferUiState {
    val selectedAccountId = accounts.firstOrNull()?.id
    val selectedRecipientId = recentRecipients.firstOrNull()?.id

    return TransferUiState(
        isLoading = false,
        accounts = accounts.map { account ->
            TransferAccountUiModel(
                id = account.id,
                name = account.name,
                balance = moneyFormatter.format(account.balanceCents, account.currency),
                type = account.type,
                isSelected = account.id == selectedAccountId,
            )
        },
        recipients = recentRecipients.map { recipient ->
            TransferRecipientUiModel(
                id = recipient.id,
                displayName = recipient.displayName,
                bankName = recipient.bankName,
                maskedIban = recipient.iban.maskIban(),
                isSelected = recipient.id == selectedRecipientId,
            )
        },
        selectedAccountId = selectedAccountId,
        selectedRecipientId = selectedRecipientId,
        statusMessage = "Kaynak hesap ve alici secildikten sonra transfer onayi verilebilir.",
    )
}

fun TransferUiState.withSelectedAccount(accountId: String): TransferUiState =
    copy(
        selectedAccountId = accountId,
        accounts = accounts.map { account ->
            account.copy(isSelected = account.id == accountId)
        },
        statusMessage = "Kaynak hesap guncellendi.",
    )

fun TransferUiState.withSelectedRecipient(recipientId: String): TransferUiState =
    copy(
        selectedRecipientId = recipientId,
        recipients = recipients.map { recipient ->
            recipient.copy(isSelected = recipient.id == recipientId)
        },
        statusMessage = "Alici bilgisi guncellendi.",
    )

private fun String.maskIban(): String {
    val compact = filterNot(Char::isWhitespace)
    if (compact.length <= 8) return this
    return "${compact.take(4)} ${"*".repeat(8)} ${compact.takeLast(4)}"
}
