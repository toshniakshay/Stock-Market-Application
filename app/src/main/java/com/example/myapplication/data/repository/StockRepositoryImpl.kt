package com.example.myapplication.data.repository

import com.example.myapplication.data.csv.CSVParser
import com.example.myapplication.data.csv.CompanyIntradayParser
import com.example.myapplication.data.local.StockDatabase
import com.example.myapplication.data.mappers.toCompanyInfoModel
import com.example.myapplication.data.mappers.toCompanyListingEntity
import com.example.myapplication.data.mappers.toComponyListingModel
import com.example.myapplication.data.remote.StockApi
import com.example.myapplication.data.remote.dto.IntradayInfoDto
import com.example.myapplication.domain.model.CompanyInfoModel
import com.example.myapplication.domain.model.CompanyListingModel
import com.example.myapplication.domain.model.IntradayInfoModel
import com.example.myapplication.domain.repository.StockRepository
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val database: StockDatabase,
    private val parser: CSVParser<CompanyListingModel>,
    private val intradayParser: CSVParser<IntradayInfoModel>
) : StockRepository {

    private val dao = database.dao;

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>> {
      return flow {
          emit(Resource.Loading(isLoading = true))
          val companyListings = dao.searchCompanyListing(query = query)
          emit((Resource.Success(data = companyListings.map { it.toComponyListingModel() })))

          val isDbEmpty = companyListings.isEmpty() && query.isBlank()
          val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote;

          if(shouldJustLoadFromCache) {
              emit(Resource.Loading(false))
              return@flow
          }

          val remoteListings = try {
              val response = api.getCompanyListings();
              parser.parseCSV(response.byteStream())
          } catch (e: IOException) {
              e.printStackTrace()
              emit(Resource.Error("Couldn't load the data"))
              null
          } catch (e: HttpException) {
              e.printStackTrace()
              emit(Resource.Error("Couldn't load the data"))
              null
          }

          remoteListings?.let {listings ->
              dao.deleteCompanyListings()
              dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
              emit(Resource.Loading(false))
              emit(Resource.Success(data = dao.searchCompanyListing("").map {
                  it.toComponyListingModel()
              }))
          }
      }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfoModel>> {
        return try {
            val response  = api.getIntradayInfo(symbol = symbol)
            val result  = intradayParser.parseCSV(response.byteStream())
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load the company intraday info")
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load the company intraday info")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfoModel> {
        return try {
            val result = api.getCompanyInfo(symbol = symbol)
            Resource.Success(result.toCompanyInfoModel())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load the company info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load the company info")
        }
    }
}