package com.example.bankingmigration

import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.domain.usecase.GetTransferInitialDataUseCase
import com.example.bankingmigration.domain.usecase.SubmitTransferUseCase
import com.example.bankingmigration.presentation.transfer.TransferViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransferViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial load selects first account and first recipient`() = runTest {
        val viewModel = transferViewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("acc-try-main", state.selectedAccountId)
        assertEquals("rec-001", state.selectedRecipientId)
        assertTrue(state.canSubmit)
    }

    @Test
    fun `submit sends selected transfer request`() = runTest {
        val repository = FakeBankingRepository()
        val viewModel = transferViewModel(repository)

        advanceUntilIdle()
        viewModel.submit()
        advanceUntilIdle()

        assertEquals("acc-try-main", repository.lastTransferRequest?.sourceAccountId)
        assertEquals("rec-001", repository.lastTransferRequest?.recipientId)
        assertEquals(100_000L, repository.lastTransferRequest?.amountCents)
    }

    private fun transferViewModel(
        repository: FakeBankingRepository = FakeBankingRepository(),
    ): TransferViewModel =
        TransferViewModel(
            getTransferInitialData = GetTransferInitialDataUseCase(repository),
            submitTransfer = SubmitTransferUseCase(repository),
            moneyFormatter = MoneyFormatter(),
        )
}
