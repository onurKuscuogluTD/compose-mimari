package com.example.bankingmigration

import com.example.bankingmigration.core.format.MoneyFormatter
import com.example.bankingmigration.domain.usecase.GetHomeSummaryUseCase
import com.example.bankingmigration.presentation.home.HomeEffect
import com.example.bankingmigration.presentation.home.HomeIntent
import com.example.bankingmigration.presentation.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial load exposes home summary ui state`() = runTest {
        val viewModel = HomeViewModel(
            getHomeSummary = GetHomeSummaryUseCase(FakeBankingRepository()),
            moneyFormatter = MoneyFormatter(),
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Onur Kuscuoglu", state.userName)
        assertEquals("2 aktif hesap gorunuyor", state.accountCountText)
    }

    @Test
    fun `retry intent loads home summary again`() = runTest {
        val repository = FakeBankingRepository()
        val viewModel = HomeViewModel(
            getHomeSummary = GetHomeSummaryUseCase(repository),
            moneyFormatter = MoneyFormatter(),
        )
        advanceUntilIdle()

        viewModel.onIntent(HomeIntent.RetryClicked)
        advanceUntilIdle()

        assertEquals(2, repository.getDashboardCallCount)
    }

    @Test
    fun `navigation intents emit one off effects`() = runTest {
        val viewModel = HomeViewModel(
            getHomeSummary = GetHomeSummaryUseCase(FakeBankingRepository()),
            moneyFormatter = MoneyFormatter(),
        )
        val effects = collectFlowValues(viewModel.effect)
        advanceUntilIdle()

        viewModel.onIntent(HomeIntent.AccountsClicked)
        viewModel.onIntent(HomeIntent.TransferClicked)
        advanceUntilIdle()

        assertEquals(
            listOf(HomeEffect.NavigateToAccounts, HomeEffect.NavigateToTransfer),
            effects,
        )
    }
}
