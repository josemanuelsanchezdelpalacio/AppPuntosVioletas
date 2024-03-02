package com.dam2jms.apppuntosvioletas.models

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam2jms.apppuntosvioletas.Manifest
import com.dam2jms.apppuntosvioletas.states.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelSOS_Screen() : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun llamada112(context: Context) {
        /*val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:608235032")

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            viewModelScope.launch(Dispatchers.Main) {
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            _uiState.value = UiState(llamada112Realizada = true)
        } else {
            Log.e("ViewModelSOS_Screen", "Permiso denegado para realizar llamadas.")
        }*/
    }

    fun alarma() {
        GlobalScope.launch(Dispatchers.IO) {
            Thread.sleep(500)
            _uiState.value = UiState(alarmaActivada = true)
        }
    }
}
