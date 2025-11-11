package com.example.test.presentation.components

data class MainUiState(
    val token: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)