package com.example.bankingmigration.presentation.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AccountsScreen(
    uiState: AccountsUiState,
    onBackClick: () -> Unit,
    onTransferClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = onBackClick) {
                Text("Geri")
            }
            Button(onClick = onTransferClick) {
                Text("Para Transferi")
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Hesaplarim",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = "Toplam TL bakiye: ${uiState.totalBalance}",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
        )

        Spacer(Modifier.height(18.dp))

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            uiState.errorMessage != null -> {
                ErrorContent(
                    message = uiState.errorMessage,
                    onRetryClick = onRetryClick,
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = uiState.accounts,
                        key = { it.id },
                    ) { account ->
                        AccountCard(account)
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountCard(
    account: AccountUiModel,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp,
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = account.type,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = account.iban,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = account.balance,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(message, color = Color(0xFFB42318))
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRetryClick) {
                Text("Tekrar Dene")
            }
        }
    }
}
