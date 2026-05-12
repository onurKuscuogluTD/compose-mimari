package com.example.bankingmigration.data.mapper

import com.example.bankingmigration.data.model.AccountDto
import com.example.bankingmigration.data.model.BankingMockDto
import com.example.bankingmigration.data.model.RecipientDto
import com.example.bankingmigration.data.model.SuggestedActionDto
import com.example.bankingmigration.data.model.UserDto
import com.example.bankingmigration.domain.model.BankAccount
import com.example.bankingmigration.domain.model.BankingDashboard
import com.example.bankingmigration.domain.model.SuggestedAction
import com.example.bankingmigration.domain.model.TransferRecipient
import com.example.bankingmigration.domain.model.UserProfile

fun BankingMockDto.toDomain(): BankingDashboard =
    BankingDashboard(
        user = user.toDomain(),
        accounts = accounts.map(AccountDto::toDomain),
        recentRecipients = recentRecipients.map(RecipientDto::toDomain),
        suggestedActions = suggestedActions.map(SuggestedActionDto::toDomain),
    )

private fun UserDto.toDomain(): UserProfile =
    UserProfile(
        id = id,
        name = name,
        segment = segment,
    )

private fun AccountDto.toDomain(): BankAccount =
    BankAccount(
        id = id,
        name = name,
        iban = iban,
        currency = currency,
        balanceCents = balanceCents,
        type = type,
    )

private fun RecipientDto.toDomain(): TransferRecipient =
    TransferRecipient(
        id = id,
        displayName = displayName,
        iban = iban,
        bankName = bankName,
    )

private fun SuggestedActionDto.toDomain(): SuggestedAction =
    SuggestedAction(
        id = id,
        title = title,
        description = description,
    )
