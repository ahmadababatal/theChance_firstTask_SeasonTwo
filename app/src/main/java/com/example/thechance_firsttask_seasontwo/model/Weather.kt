package com.example.thechance_firsttask_seasontwo.model

data class Weather(
    val coord : Coord,
    val weather: List<WeatherItem>,
    val main: Main,
    val visibility: String,
    val wind: Wind,
    val clouds : Cloud
)