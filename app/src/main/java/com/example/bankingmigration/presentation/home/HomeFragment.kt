package com.example.bankingmigration.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bankingmigration.R
import com.example.bankingmigration.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accountsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_accountsFragment)
        }
        binding.transferButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_transferFragment)
        }
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(uiState: HomeUiState) = with(binding) {
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
