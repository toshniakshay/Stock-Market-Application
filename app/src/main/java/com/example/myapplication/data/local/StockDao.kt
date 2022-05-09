package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertCompanyListings(
        companyList: List<CompanyListingEntity>
    )

    @Query("Delete from companylistingentity")
    suspend fun deleteCompanyListings()

    @Query("""SELECT *
        FROM companylistingentity
        WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
        UPPER(:query) == symbol""")
    suspend fun searchCompanyListing(
        query: String
    ): List<CompanyListingEntity>
}