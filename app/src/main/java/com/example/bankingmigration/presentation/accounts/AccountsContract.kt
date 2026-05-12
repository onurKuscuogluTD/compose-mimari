package com.example.bankingmigration.presentation.accounts

import com.example.bankingmigration.core.mvi.MviEffect
import com.example.bankingmigration.core.mvi.MviIntent

sealed interface AccountsIntent : MviIntent {
    data object Load : AccountsIntent
    data object RetryClicked : AccountsIntent
    data object BackClicked : AccountsIntent
    data object TransferClicked : AccountsIntent
}

sealed interface AccountsEffect : MviEffect {
    data object NavigateBack : AccountsEffect
    data object NavigateToTransfer : AccountsEffect
}
