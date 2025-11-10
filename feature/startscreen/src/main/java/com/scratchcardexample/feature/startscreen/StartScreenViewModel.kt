package com.scratchcardexample.feature.startscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    repo: ScratchCodeRepository,
): ViewModel() {
    val scratchcardStateId: StateFlow<Int> = combine(repo.isCardActivated(), repo.readCode()) { isActivated, code ->
        if (isActivated)
            R.string.card_activated
        else
        if (code.isNullOrEmpty())
            R.string.card_not_scratched
        else
            R.string.card_scratched
    }
    .stateIn(viewModelScope, SharingStarted.Lazily, R.string.card_not_scratched)
}