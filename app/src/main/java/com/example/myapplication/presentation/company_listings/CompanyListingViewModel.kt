package com.example.myapplication.presentation.company_listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.StockRepository
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val stockRepository: StockRepository
): ViewModel(){

   var state by mutableStateOf(CompanyListingState())

    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingEvent) {
        when(event) {
            is CompanyListingEvent.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    getCompanyListings(event.query, false)
                }
            }
            CompanyListingEvent.RefreshCompanyListings -> {
                getCompanyListings(fetchFromRemote = true)
            }
        }
    }

    fun getCompanyListings(
        query: String = state.searchQuery.lowercase(), fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            stockRepository
                .getCompanyListings(fetchFromRemote = fetchFromRemote, query = query)
                .collect {result ->
                    when(result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                        is Resource.Success -> {
                            result.data?.let { companyList ->
                                state = state.copy(companyList = companyList)
                            }
                        }
                    }
                }

        }
    }
}