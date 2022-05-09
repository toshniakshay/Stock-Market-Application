package com.example.myapplication.presentation.company_listings

import com.example.myapplication.domain.model.CompanyListingModel

data class CompanyListingState(
    val companyList: List<CompanyListingModel> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery : String = ""
)