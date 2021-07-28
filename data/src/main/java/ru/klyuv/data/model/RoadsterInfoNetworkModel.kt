package ru.klyuv.data.model

import com.google.gson.annotations.SerializedName

data class RoadsterInfoNetworkModel(
    @SerializedName("flickr_images") val flickrImages: List<String>?,
    @SerializedName("name") val name: String?,
    @SerializedName("launch_date_utc") val launchDate: String?,
    @SerializedName("launch_mass_kg") val launchMassKg: Short?,
    @SerializedName("epoch_jd") val epochJd: Double?,
    @SerializedName("orbit_type") val orbitType: String?,
    @SerializedName("apoapsis_au") val apoapsisAu: Double?,
    @SerializedName("periapsis_au") val periapsisAu: Double?,
    @SerializedName("semi_major_axis_au") val semiMajorAxisAu: Double?,
    @SerializedName("eccentricity") val eccentricity: Double?,
    @SerializedName("inclination") val inclination: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("periapsis_arg") val periapsisArg: Double?,
    @SerializedName("period_days") val periodDays: Double?,
    @SerializedName("speed_kph") val speedKph: Double?,
    @SerializedName("earth_distance_km") val earthDistanceKm: Double?,
    @SerializedName("mars_distance_km") val marsDistanceKm: Double?,
    @SerializedName("wikipedia") val wikipediaUrl: String?,
    @SerializedName("video") val videoUrl: String?,
    @SerializedName("details") val details: String?,
    @SerializedName("id") val id: String?
)