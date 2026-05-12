package com.example.bankingmigration.presentation.home

import com.example.bankingmigration.core.mvi.MviEffect
import com.example.bankingmigration.core.mvi.MviIntent

sealed interface HomeIntent : MviIntent {
    data object Load : HomeIntent
    data object RetryClicked : HomeIntent
    data object AccountsClicked : HomeIntent
    data object TransferClicked : HomeIntent
}

sealed interface HomeEffect : MviEffect {
    data object NavigateToAccounts : HomeEffect
    data object NavigateToTransfer : HomeEffect
}
