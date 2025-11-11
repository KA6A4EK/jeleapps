package com.example.test.presentation

import androidx.lifecycle.ViewModel
import com.example.test.presentation.components.MainUiState
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    fun onNotificationPermissionResult(granted: Boolean) {
        if (granted) fetchToken()
    }

    fun fetchToken() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        firebaseMessaging.token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = MainUiState(token = task.result)
                } else {
                    _uiState.value = MainUiState(error = "Error getting token")
                }
            }
    }
}