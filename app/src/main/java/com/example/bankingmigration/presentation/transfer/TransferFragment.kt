package com.example.bankingmigration.presentation.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bankingmigration.core.design.BankingTheme
import com.example.bankingmigration.databinding.FragmentTransferBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferFragment : Fragment() {

    private var _binding: FragmentTransferBinding? = null
    private val binding: FragmentTransferBinding
        get() = checkNotNull(_binding)

    private val viewModel: TransferViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        setupComposeSections()
        collectXmlState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupComposeSections() {
        binding.sourceAccountComposeView.apply {
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

        binding.recipientsComposeView.apply {
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

        binding.transferSummaryComposeView.apply {
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

    private fun collectXmlState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.xmlStatusTextView.text = uiState.statusMessage
                }
            }
        }
    }
}
