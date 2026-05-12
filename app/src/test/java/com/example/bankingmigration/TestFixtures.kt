package com.example.bankingmigration

import com.example.bankingmigration.domain.model.BankAccount
import com.example.bankingmigration.domain.model.BankingDashboard
import com.example.bankingmigration.domain.model.SuggestedAction
import com.example.bankingmigration.domain.model.TransferReceipt
import com.example.bankingmigration.domain.model.TransferRecipient
import com.example.bankingmigration.domain.model.TransferRequest
import com.example.bankingmigration.domain.model.UserProfile
import com.example.bankingmigration.domain.repository.BankingRepository

fun sampleDashboard(): BankingDashboard =
    BankingDashboard(
        user = UserProfile(
            id = "user-001",
            name = "Onur Kuscuoglu",
            segment = "Premium",
        ),
        accounts = listOf(
            BankAccount(
                id = "acc-try-main",
                name = "Vadesiz TL Hesabi",
                iban = "TR12 0001 0000 0000 0000 0001",
                currency = "TRY",
                balanceCents = 125_430_75,
                type = "Current",
            ),
            BankAccount(
                id = "acc-usd-saving",
                name = "Birikim USD Hesabi",
                iban = "TR12 0001 0000 0000 0000 0002",
                currency = "USD",
                balanceCents = 842_050,
                type = "Savings",
            ),
        ),
        recentRecipients = listOf(
            TransferRecipient(
                id = "rec-001",
                displayName = "Ayse Demir",
                iban = "TR44 0001 0000 0000 0000 1001",
                bankName = "Ornek Bank",
            ),
        ),
        suggestedActions = listOf(
            SuggestedAction(
                id = "act-001",
                title = "Fatura talimatlarini kontrol et",
                description = "Yaklasan otomatik odemelerini incele.",
            ),
        ),
    )

class FakeBankingRepository(
    private val dashboard: BankingDashboard = sampleDashboard(),
    private val transferReceipt: TransferReceipt = TransferReceipt(
        referenceCode = "BNK-123456",
        statusMessage = "Transfer talebi alindi. Referans: BNK-123456",
    ),
) : BankingRepository {
    var lastTransferRequest: TransferRequest? = null
        private set

    override suspend fun getDashboard(): BankingDashboard = dashboard

    override suspend fun submitTransfer(request: TransferRequest): TransferReceipt {
        lastTransferRequest = request
        return transferReceipt
    }
}
