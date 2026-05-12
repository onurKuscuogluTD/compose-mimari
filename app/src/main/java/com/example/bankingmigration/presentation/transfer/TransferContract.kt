package com.example.bankingmigration.presentation.transfer

import com.example.bankingmigration.core.mvi.MviEffect
import com.example.bankingmigration.core.mvi.MviIntent

sealed interface TransferIntent : MviIntent {
    data object Load : TransferIntent
    data object RetryClicked : TransferIntent
    data object BackClicked : TransferIntent
    data class AccountSelected(val accountId: String) : TransferIntent
    data class RecipientSelected(val recipientId: String) : TransferIntent
    data object SubmitClicked : TransferIntent
}

sealed interface TransferEffect : MviEffect {
    data object NavigateBack : TransferEffect
}
