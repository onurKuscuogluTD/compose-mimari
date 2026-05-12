package com.example.bankingmigration.presentation.home

data class HomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val segment: String = "",
    val totalBalance: String = "",
    val accountCountText: String = "",
    val suggestedActions: List<HomeSuggestedActionUiModel> = emptyList(),
    val errorMessage: String? = null,
)

data class HomeSuggestedActionUiModel(
    val id: String,
    val title: String,
    val description: String,
)
