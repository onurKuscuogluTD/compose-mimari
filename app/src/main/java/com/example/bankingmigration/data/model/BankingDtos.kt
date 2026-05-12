package com.example.bankingmigration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BankingMockDto(
    val user: UserDto,
    val accounts: List<AccountDto>,
    val recentRecipients: List<RecipientDto>,
    val suggestedActions: List<SuggestedActionDto>,
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val segment: String,
)

@Serializable
data class AccountDto(
    val id: String,
    val name: String,
    val iban: String,
    val currency: String,
    val balanceCents: Long,
    val type: String,
)

@Serializable
data class RecipientDto(
    val id: String,
    val displayName: String,
    val iban: String,
    val bankName: String,
)

@Serializable
data class SuggestedActionDto(
    val id: String,
    val title: String,
    val description: String,
)
