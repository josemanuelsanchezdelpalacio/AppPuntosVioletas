package com.dam2jms.apppuntosvioletas.models

import androidx.lifecycle.ViewModel
import com.dam2jms.apppuntosvioletas.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelFirstScreen : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

}