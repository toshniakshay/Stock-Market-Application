package com.example.myapplication.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.remote.dto.CompanyInfoDto
import com.example.myapplication.data.remote.dto.IntradayInfoDto
import com.example.myapplication.domain.model.CompanyInfoModel
import com.example.myapplication.domain.model.IntradayInfoModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun IntradayInfoDto.toIntradayModel(): IntradayInfoModel {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timeStamp, formatter)

    return  IntradayInfoModel(
        closingValue = closingValue,
        timeStamp = localDateTime
    )
}

fun IntradayInfoModel.toIntradayDto(): IntradayInfoDto {

    return IntradayInfoDto(
        closingValue = closingValue,
        timeStamp = timeStamp.toString()
    )
}

fun CompanyInfoDto.toCompanyInfoModel(): CompanyInfoModel {
    return CompanyInfoModel(
        name = name?: "",
        symbol = symbol ?: "",
        description = description ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}

fun CompanyInfoModel.toCompanyInfoDto(): CompanyInfoDto {
    return CompanyInfoDto(
        name = name,
        symbol = symbol,
        description = description,
        country = country,
        industry = industry
    )
}