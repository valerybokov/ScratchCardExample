package com.scratchcardexample.feature.startscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class StartScreenViewModel @Inject constructor(): ViewModel() {
    val scratchcardStateId: StateFlow<Int> = flow<Int>{ R.string.card_activated }.stateIn(viewModelScope, SharingStarted.Lazily, R.string.card_not_scratched)
}