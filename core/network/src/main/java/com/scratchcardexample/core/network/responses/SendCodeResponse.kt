package com.scratchcardexample.core.network.responses

import com.squareup.moshi.Json

data class SendCodeResponse(
    @param:Json(name = "android")
    val android: String
)