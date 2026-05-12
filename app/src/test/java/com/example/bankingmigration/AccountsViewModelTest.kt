package com.example.bankingmigration

import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.domain.usecase.GetAccountsUseCase
import com.example.bankingmigration.presentation.accounts.AccountsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial load exposes account list ui state`() = runTest {
        val viewModel = AccountsViewModel(
            getAccounts = GetAccountsUseCase(FakeBankingRepository()),
            moneyFormatter = MoneyFormatter(),
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.accounts.size)
        assertEquals("Vadesiz TL Hesabi", state.accounts.first().name)
    }
}
