package com.example.myapplication.domain.model

import com.example.myapplication.data.local.CompanyListingEntity

data class CompanyListingModel (
    val name: String,
    val symbol: String,
    val exchange: String,
)