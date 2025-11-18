package com.charly.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TempData(

    @SerialName("day")
    val day: String? = null,
    @SerialName("min")
    val min: String? = null,
    @SerialName("max")
    val max: String? = null,
    @SerialName("night")
    val night: String? = null,
    @SerialName("eve")
    val eve: String? = null,
    @SerialName("morn")
    val morn: String? = null
)
