package com.example.bankingmigration.domain.model

data class BankingDashboard(
    val user: UserProfile,
    val accounts: List<BankAccount>,
    val recentRecipients: List<TransferRecipient>,
    val suggestedActions: List<SuggestedAction>,
)

data class UserProfile(
    val id: String,
    val name: String,
    val segment: String,
)

data class BankAccount(
    val id: String,
    val name: String,
    val iban: String,
    val currency: String,
    val balanceCents: Long,
    val type: String,
)

data class TransferRecipient(
    val id: String,
    val displayName: String,
    val iban: String,
    val bankName: String,
)

data class SuggestedAction(
    val id: String,
    val title: String,
    val description: String,
)

data class TransferRequest(
    val sourceAccountId: String,
    val recipientId: String,
    val amountCents: Long,
)

data class TransferReceipt(
    val referenceCode: String,
    val statusMessage: String,
)
