package com.example.myapplication.data.csv

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.mappers.toIntradayModel
import com.example.myapplication.data.remote.dto.IntradayInfoDto
import com.example.myapplication.domain.model.IntradayInfoModel
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyIntradayParser @Inject constructor(): CSVParser<IntradayInfoModel> {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun parseCSV(stream: InputStream): List<IntradayInfoModel> {
        val csvReader = CSVReader(InputStreamReader(stream))

        return withContext(Dispatchers.IO) {
            csvReader.readAll().drop(1).mapNotNull { line ->
                val closing = line.getOrNull(4) ?: return@mapNotNull null
                val timeStamp = line.getOrNull(0) ?: return@mapNotNull null

                val dto = IntradayInfoDto(
                    timeStamp = timeStamp,
                    closingValue = closing.toDouble()
                )

                dto.toIntradayModel()
            }
        }.filter {
            it.timeStamp.dayOfMonth == LocalDateTime.now().minusDays(4).dayOfMonth
        }.sortedBy {
            it.timeStamp.hour
        }.also {
            csvReader.close()
        }
    }
}