package com.example.scratchcardexample

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel() {
    private val _header: MutableState<Int> =
        mutableIntStateOf(R.string.app_name)

    val header: State<Int> = _header

    val showBackButton: Boolean
        get() = _header.value != R.string.app_name

    fun updateHeader(route: String) {
        val value = when(route) {
            ROUTE_ACTIVATION -> com.example.scratchcardexample.feature.activation.R.string.title_activation
            ROUTE_SCRATCH -> com.example.scratchcardexample.feature.scratchscreen.R.string.scratch
            else -> R.string.app_name
        }
        if (_header.value != value)
            _header.value = value
    }
}