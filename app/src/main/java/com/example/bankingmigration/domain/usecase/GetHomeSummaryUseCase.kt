package com.example.bankingmigration.domain.usecase

import com.example.bankingmigration.domain.model.BankingDashboard
import com.example.bankingmigration.domain.repository.BankingRepository
import javax.inject.Inject

class GetHomeSummaryUseCase @Inject constructor(
    private val repository: BankingRepository,
) {
    suspend operator fun invoke(): BankingDashboard = repository.getDashboard()
}
