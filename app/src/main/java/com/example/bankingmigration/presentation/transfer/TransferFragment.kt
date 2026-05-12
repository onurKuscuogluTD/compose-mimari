package com.example.bankingmigration.presentation.transfer

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bankingmigration.core.base.BaseViewBindingFragment
import com.example.bankingmigration.core.design.BankingTheme
import com.example.bankingmigration.databinding.FragmentTransferBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferFragment : BaseViewBindingFragment<FragmentTransferBinding>(
    FragmentTransferBinding::inflate,
) {

    private val viewModel: TransferViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        setupComposeSections()
        collectXmlState()
    }

    private fun setupComposeSections() {
        withBinding {
            sourceAccountComposeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    BankingTheme {
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        SourceAccountSection(
                            accounts = uiState.accounts,
                            isLoading = uiState.isLoading,
                            onAccountSelected = viewModel::selectAccount,
                        )
                    }
                }
            }

            recipientsComposeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    BankingTheme {
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        RecentRecipientsSection(
                            recipients = uiState.recipients,
                            isLoading = uiState.isLoading,
                            onRecipientSelected = viewModel::selectRecipient,
                        )
                    }
                }
            }

            transferSummaryComposeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    BankingTheme {
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        TransferSummarySection(
                            uiState = uiState,
                            onSubmitClick = viewModel::submit,
                            onRetryClick = viewModel::retry,
                        )
                    }
                }
            }
        }
    }

    private fun collectXmlState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    withBinding {
                        xmlStatusTextView.text = uiState.statusMessage
                    }
                }
            }
        }
    }
}
