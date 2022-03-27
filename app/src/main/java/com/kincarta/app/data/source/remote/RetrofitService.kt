package com.kincarta.app.source.remote

import com.kincarta.app.domain.model.CaseStudiesResponse
import io.reactivex.Single
import retrofit2.http.GET

interface RetrofitService {

    @GET("theappbusiness/engineering-challenge/main/endpoints/v1/caseStudies.json")
    fun getCaseStudies(): Single<CaseStudiesResponse>
}