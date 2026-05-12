package com.example.bankingmigration.presentation.accounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AccountsRoute(
    onBackClick: () -> Unit,
    onTransferClick: () -> Unit,
    viewModel: AccountsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AccountsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onTransferClick = onTransferClick,
        onRetryClick = viewModel::retry,
    )
}
