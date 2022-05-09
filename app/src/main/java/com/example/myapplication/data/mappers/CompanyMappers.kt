package com.example.myapplication.data.mappers

import com.example.myapplication.data.local.CompanyListingEntity
import com.example.myapplication.data.remote.dto.CompanyInfoDto
import com.example.myapplication.domain.model.CompanyInfoModel
import com.example.myapplication.domain.model.CompanyListingModel

fun CompanyListingEntity.toComponyListingModel():CompanyListingModel {
    return CompanyListingModel(
        name = name,
        symbol = symbol,
        exchange = exchange,
    )
}

fun CompanyListingModel.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}
