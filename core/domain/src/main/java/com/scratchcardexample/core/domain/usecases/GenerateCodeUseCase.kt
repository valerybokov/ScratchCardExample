package com.scratchcardexample.core.domain.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class GenerateCodeUseCase @Inject constructor() {
    suspend operator fun invoke(): String {
        //Let's pretend this is a heavy operation and takes 2 seconds.
        delay(2000)
        return UUID.randomUUID().toString()
    }
}