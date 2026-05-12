package com.example.bankingmigration.domain.usecase

import com.example.bankingmigration.domain.model.BankAccount
import com.example.bankingmigration.domain.model.TransferRecipient
import com.example.bankingmigration.domain.repository.BankingRepository
import javax.inject.Inject

data class TransferInitialData(
    val accounts: List<BankAccount>,
    val recentRecipients: List<TransferRecipient>,
)

class GetTransferInitialDataUseCase @Inject constructor(
    private val repository: BankingRepository,
) {
    suspend operator fun invoke(): TransferInitialData {
        val dashboard = repository.getDashboard()
        return TransferInitialData(
            accounts = dashboard.accounts,
            recentRecipients = dashboard.recentRecipients,
        )
    }
}
