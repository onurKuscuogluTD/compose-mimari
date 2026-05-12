package com.example.bankingmigration.presentation.accounts

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import com.example.bankingmigration.R
import com.example.bankingmigration.core.base.BaseComposeFragment
import com.example.bankingmigration.core.design.BankingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment : BaseComposeFragment() {

    @Composable
    override fun Content() {
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
