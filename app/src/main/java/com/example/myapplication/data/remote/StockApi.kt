package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getCompanyListings(
        @Query("apikey") apikey: String = API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol:String,
        @Query("apikey") apikey: String = API_KEY,
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
     @Query("apikey") apiKey: String = API_KEY,
     @Query("symbol") symbol: String
    ): CompanyInfoDto

    companion object {
        const val BASE_URL = "https://alphavantage.co"
        const val API_KEY = "KWWM949CWC6UAXBX"
    }
}