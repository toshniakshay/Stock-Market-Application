package com.example.myapplication.presentation.company_details

import com.example.myapplication.domain.model.CompanyInfoModel
import com.example.myapplication.domain.model.IntradayInfoModel

data class CompanyInfoState(
    val intradayInfo: List<IntradayInfoModel> = emptyList(),
    val company: CompanyInfoModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)