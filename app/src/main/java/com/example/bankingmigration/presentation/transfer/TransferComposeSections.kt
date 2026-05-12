package com.example.bankingmigration.presentation.transfer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SourceAccountSection(
    accounts: List<TransferAccountUiModel>,
    isLoading: Boolean,
    onAccountSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionSurface(modifier) {
        SectionTitle("Kaynak Hesap")
        Spacer(Modifier.height(12.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(
                    items = accounts,
                    key = { it.id },
                ) { account ->
                    SelectableAccountCard(
                        account = account,
                        onClick = { onAccountSelected(account.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun RecentRecipientsSection(
    recipients: List<TransferRecipientUiModel>,
    isLoading: Boolean,
    onRecipientSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionSurface(modifier) {
        SectionTitle("Son Alicilar")
        Spacer(Modifier.height(12.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                recipients.forEach { recipient ->
                    SelectableRecipientRow(
                        recipient = recipient,
                        onClick = { onRecipientSelected(recipient.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun TransferSummarySection(
    uiState: TransferUiState,
    onSubmitClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionSurface(modifier) {
        SectionTitle("Transfer Ozeti")
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Tutar: ${uiState.amountText}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = uiState.statusMessage,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
        )
        uiState.errorMessage?.let { message ->
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = message,
                color = Color(0xFFB42318),
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                enabled = uiState.canSubmit,
                onClick = onSubmitClick,
            ) {
                Text(if (uiState.isSubmitting) "Gonderiliyor" else "Transferi Onayla")
            }
            if (uiState.errorMessage != null) {
                Button(onClick = onRetryClick) {
                    Text("Tekrar Dene")
                }
            }
        }
    }
}

@Composable
private fun SelectableAccountCard(
    account: TransferAccountUiModel,
    onClick: () -> Unit,
) {
    val borderColor = if (account.isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.24f)
    }
    Surface(
        modifier = Modifier
            .width(260.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (account.isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.White,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = account.type,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = account.balance,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun SelectableRecipientRow(
    recipient: TransferRecipientUiModel,
    onClick: () -> Unit,
) {
    val borderColor = if (recipient.isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (recipient.isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.White,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text = recipient.displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = recipient.bankName,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = recipient.maskedIban,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
            )
        }
    }
}

@Composable
private fun SectionSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 1.dp,
    ) {
        Column(Modifier.padding(18.dp)) {
            content()
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
    )
}
