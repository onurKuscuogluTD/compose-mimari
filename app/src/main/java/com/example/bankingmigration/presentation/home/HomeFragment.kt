package com.example.bankingmigration.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bankingmigration.R
import com.example.bankingmigration.core.base.BaseViewBindingFragment
import com.example.bankingmigration.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseViewBindingFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate,
) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            accountsButton.setOnClickListener {
                viewModel.onIntent(HomeIntent.AccountsClicked)
            }
            transferButton.setOnClickListener {
                viewModel.onIntent(HomeIntent.TransferClicked)
            }
        }
        collectUiState()
        collectEffects()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun collectEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        HomeEffect.NavigateToAccounts -> {
                            findNavController().navigate(R.id.action_homeFragment_to_accountsFragment)
                        }
                        HomeEffect.NavigateToTransfer -> {
                            findNavController().navigate(R.id.action_homeFragment_to_transferFragment)
                        }
                    }
                }
            }
        }
    }

    private fun render(uiState: HomeUiState) = withBinding {
        loadingTextView.isVisible = uiState.isLoading
        errorTextView.isVisible = uiState.errorMessage != null
        errorTextView.text = uiState.errorMessage.orEmpty()

        greetingTextView.text = if (uiState.userName.isBlank()) {
            "Merhaba"
        } else {
            "Merhaba, ${uiState.userName}"
        }
        segmentTextView.text = uiState.segment
        totalBalanceTextView.text = uiState.totalBalance
        accountsCountTextView.text = uiState.accountCountText

        val firstAction = uiState.suggestedActions.getOrNull(0)
        val secondAction = uiState.suggestedActions.getOrNull(1)
        firstSuggestionTextView.text = firstAction?.let { "${it.title}\n${it.description}" }.orEmpty()
        secondSuggestionTextView.text = secondAction?.let { "${it.title}\n${it.description}" }.orEmpty()
    }
}
