package com.example.myapplication.presentation.company_listings

sealed class CompanyListingEvent {
    object RefreshCompanyListings : CompanyListingEvent()
    data class OnSearchQueryChanged(val query: String): CompanyListingEvent()
}
