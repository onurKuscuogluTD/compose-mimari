package com.example.bankingmigration.domain.usecase

import com.example.bankingmigration.domain.model.BankAccount
import com.example.bankingmigration.domain.repository.BankingRepository
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val repository: BankingRepository,
) {
    suspend operator fun invoke(): List<BankAccount> = repository.getDashboard().accounts
}
