package com.example.myapplication.domain.model

import java.time.LocalDateTime

data class IntradayInfoModel(
    val timeStamp: LocalDateTime,
    val closingValue: Double
)