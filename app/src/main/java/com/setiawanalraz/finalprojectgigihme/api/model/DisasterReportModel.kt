package com.setiawanalraz.finalprojectgigihme.api.model

import com.google.gson.annotations.SerializedName

data class DisasterReportModel(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("result")
    val result: Result
)

data class Result(
    @SerializedName("type")
    val type: String,
    @SerializedName("objects")
    val objects: Objects
)

data class Objects(
    @SerializedName("output")
    val output: Output
)

data class Output(
    @SerializedName("type")
    val type: String,
    @SerializedName("geometries")
    val geometries: List<Geometry>
)

data class Geometry(
    @SerializedName("type")
    val type: String,
    @SerializedName("properties")
    val disasterReports: DisasterReports,
    @SerializedName("coordinates")
    val coordinates: List<Double>
)

data class DisasterReports(
    @SerializedName("pkey")
    val pkey: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("disaster_type")
    val disasterType: String,
    @SerializedName("report_data")
    val reportData: ReportData,
    @SerializedName("tags")
    val tags: Tags,
    @SerializedName("title")
    val title: String?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("partner_code")
    val partnerCode: String?,
    @SerializedName("partner_icon")
    val partnerIcon: String?
)

data class ReportData(
    @SerializedName("report_type")
    val reportType: String,
    @SerializedName("flood_depth")
    val flood_depth: Int
)

data class Tags(
    @SerializedName("district_id")
    val districtId: String?,
    @SerializedName("region_code")
    val regionCode: String?,
    @SerializedName("local_area_id")
    val localAreaId: String?,
    @SerializedName("instance_region_code")
    val instanceRegionCode: String?
)