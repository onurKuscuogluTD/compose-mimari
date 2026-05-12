package com.example.bankingmigration.presentation.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bankingmigration.R
import com.example.bankingmigration.core.design.BankingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
            )
            setContent {
                BankingTheme {
                    AccountsRoute(
                        onBackClick = { findNavController().popBackStack() },
                        onTransferClick = {
                            findNavController().navigate(
                                R.id.action_accountsFragment_to_transferFragment,
                            )
                        },
                    )
                }
            }
        }
    }
}
