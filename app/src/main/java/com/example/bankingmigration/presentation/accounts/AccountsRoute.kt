package com.example.bankingmigration.presentation.accounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bankingmigration.core.mvi.CollectEffectWithLifecycle

@Composable
fun AccountsRoute(
    onBackClick: () -> Unit,
    onTransferClick: () -> Unit,
    viewModel: AccountsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEffectWithLifecycle(viewModel.effect) { effect ->
        when (effect) {
            AccountsEffect.NavigateBack -> onBackClick()
            AccountsEffect.NavigateToTransfer -> onTransferClick()
        }
    }

    AccountsScreen(
        uiState = uiState,
        onBackClick = { viewModel.onIntent(AccountsIntent.BackClicked) },
        onTransferClick = { viewModel.onIntent(AccountsIntent.TransferClicked) },
        onRetryClick = { viewModel.onIntent(AccountsIntent.RetryClicked) },
    )
}
