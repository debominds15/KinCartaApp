package com.kincarta.app.domain.model

import com.google.gson.annotations.SerializedName


data class CaseStudies(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("client")
    var client: String? = "",
    @SerializedName("teaser")
    var teaser: String? = "",
    @SerializedName("vertical")
    var vertical: String? = "",
    @SerializedName("is_enterprise")
    var isEnterprise: Boolean? = false,
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("hero_image")
    var heroImage: String? = "",
    @SerializedName("sections")
    var sections: List<Sections>? = listOf(),
    @SerializedName("app_store_url")
    var appStoreUrl: String? = "",
)