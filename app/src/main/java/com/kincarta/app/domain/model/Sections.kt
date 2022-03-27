package com.kincarta.app.domain.model

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class Sections(
    @SerializedName("title")
    var title: String? = null,

    @SerializedName("body_elements")
    val bodyElements: List<JsonElement>? = emptyList()
)