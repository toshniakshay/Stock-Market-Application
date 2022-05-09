package com.example.myapplication.di

import com.example.myapplication.data.csv.CSVParser
import com.example.myapplication.data.csv.CompanyIntradayParser
import com.example.myapplication.data.csv.CompanyListingParser
import com.example.myapplication.data.repository.StockRepositoryImpl
import com.example.myapplication.domain.model.CompanyListingModel
import com.example.myapplication.domain.model.IntradayInfoModel
import com.example.myapplication.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCSVParser(
        csvParser: CompanyListingParser
    ): CSVParser<CompanyListingModel>

    @Binds
    @Singleton
    abstract fun bindIntradayCSVParser(
        csvParser: CompanyIntradayParser
    ): CSVParser<IntradayInfoModel>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepository: StockRepositoryImpl
    ): StockRepository
}