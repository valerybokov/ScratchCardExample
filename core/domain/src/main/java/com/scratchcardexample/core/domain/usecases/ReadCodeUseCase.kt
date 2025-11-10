package com.scratchcardexample.core.domain.usecases

import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@ViewModelScoped
class ReadCodeUseCase @Inject constructor(
    private val repo: ScratchCodeRepository,
) {
    suspend operator fun invoke(): String? =
        repo.readCode().firstOrNull()
}