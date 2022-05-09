package com.example.myapplication.presentation.company_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.StockRepository
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val stockRepository: StockRepository
): ViewModel(){

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)

            val companyInfoResult = async { stockRepository.getCompanyInfo(symbol = symbol) }
            val companyIntradayResult = async { stockRepository.getIntradayInfo(symbol = symbol) }

            when(val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(isLoading = false, company = result.data, error = null)
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false, company = null, error = result.message)
                }

                else -> Unit
            }

            when(val result = companyIntradayResult.await()) {
                is Resource.Success -> {
                    state = state.copy(isLoading = false, intradayInfo = result.data ?: emptyList(), error = null)
                }

                is Resource.Error-> {
                    state = state.copy(isLoading = false, company = null, error = result.message)
                }

                else -> Unit
            }


        }
    }


}