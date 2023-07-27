package com.setiawanalraz.finalprojectgigihme.api.model

data class DisasterReportModel(
    val title: String,
    val body: String,
    val imgUrl: String?,
    val coordinates: Coordinates,
)

data class Coordinates (
    val lat: Double,
    val lng: Double,
)