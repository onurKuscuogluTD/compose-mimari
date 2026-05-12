package com.example.bankingmigration.domain.repository

import com.example.bankingmigration.domain.model.BankingDashboard
import com.example.bankingmigration.domain.model.TransferReceipt
import com.example.bankingmigration.domain.model.TransferRequest

interface BankingRepository {
    suspend fun getDashboard(): BankingDashboard
    suspend fun submitTransfer(request: TransferRequest): TransferReceipt
}
