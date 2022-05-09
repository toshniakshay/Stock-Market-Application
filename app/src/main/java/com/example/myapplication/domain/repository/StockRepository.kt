package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.CompanyInfoModel
import com.example.myapplication.domain.model.CompanyListingModel
import com.example.myapplication.domain.model.IntradayInfoModel
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfoModel>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfoModel>
}