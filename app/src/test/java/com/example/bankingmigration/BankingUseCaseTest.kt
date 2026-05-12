package com.example.bankingmigration

import com.example.bankingmigration.domain.model.TransferRequest
import com.example.bankingmigration.domain.usecase.GetAccountsUseCase
import com.example.bankingmigration.domain.usecase.GetHomeSummaryUseCase
import com.example.bankingmigration.domain.usecase.GetTransferInitialDataUseCase
import com.example.bankingmigration.domain.usecase.SubmitTransferUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class BankingUseCaseTest {

    private val repository = FakeBankingRepository()

    @Test
    fun `home summary returns dashboard from repository`() = runTest {
        val result = GetHomeSummaryUseCase(repository)()

        assertEquals("Onur Kuscuoglu", result.user.name)
    }

    @Test
    fun `accounts use case exposes account list only`() = runTest {
        val result = GetAccountsUseCase(repository)()

        assertEquals(2, result.size)
    }

    @Test
    fun `transfer initial data combines accounts and recipients`() = runTest {
        val result = GetTransferInitialDataUseCase(repository)()

        assertEquals(2, result.accounts.size)
        assertEquals(2, result.recentRecipients.size)
    }

    @Test
    fun `submit transfer delegates request to repository`() = runTest {
        val request = TransferRequest(
            sourceAccountId = "acc-try-main",
            recipientId = "rec-001",
            amountCents = 100_000,
        )

        val receipt = SubmitTransferUseCase(repository)(request)

        assertEquals(request, repository.lastTransferRequest)
        assertEquals("BNK-123456", receipt.referenceCode)
    }
}
