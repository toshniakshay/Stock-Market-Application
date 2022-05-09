package com.example.myapplication.data.csv

import java.io.InputStream


interface CSVParser<T> {

    suspend fun parseCSV(stream: InputStream): List<T>
}