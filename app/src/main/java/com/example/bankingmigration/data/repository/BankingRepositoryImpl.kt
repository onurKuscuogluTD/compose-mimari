package com.example.bankingmigration.data.repository

import com.example.bankingmigration.data.datasource.BankingLocalDataSource
import com.example.bankingmigration.data.mapper.toDomain
import com.example.bankingmigration.domain.model.BankingDashboard
import com.example.bankingmigration.domain.model.TransferReceipt
import com.example.bankingmigration.domain.model.TransferRequest
import com.example.bankingmigration.domain.repository.BankingRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class BankingRepositoryImpl @Inject constructor(
    private val localDataSource: BankingLocalDataSource,
) : BankingRepository {

    override suspend fun getDashboard(): BankingDashboard =
        localDataSource.getBankingPayload().toDomain()

    override suspend fun submitTransfer(request: TransferRequest): TransferReceipt {
        delay(350)
        val reference = "BNK-${Random.nextInt(100_000, 999_999)}"
        return TransferReceipt(
            referenceCode = reference,
            statusMessage = "${request.recipientId} icin transfer talebi alindi. Referans: $reference",
        )
    }
}
