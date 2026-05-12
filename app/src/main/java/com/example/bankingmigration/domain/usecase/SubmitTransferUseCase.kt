package com.example.bankingmigration.domain.usecase

import com.example.bankingmigration.domain.model.TransferReceipt
import com.example.bankingmigration.domain.model.TransferRequest
import com.example.bankingmigration.domain.repository.BankingRepository
import javax.inject.Inject

class SubmitTransferUseCase @Inject constructor(
    private val repository: BankingRepository,
) {
    suspend operator fun invoke(request: TransferRequest): TransferReceipt =
        repository.submitTransfer(request)
}
