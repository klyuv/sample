package ru.klyuv.core.model

import android.content.Context
import androidx.annotation.StringRes
import ru.klyuv.core.R
import ru.klyuv.core.common.extensions.formatDate

data class RoadsterInfoModel(
    val flickrImages: List<String>,
    val name: String,
    val launchDate: String,
    val launchMassKg: Short,
    val epochJd: Double,
    val orbitType: String,
    val apoapsisAu: Double,
    val periapsisAu: Double,
    val semiMajorAxisAu: Double,
    val eccentricity: Double,
    val inclination: Double,
    val longitude: Double,
    val periapsisArg: Double,
    val periodDays: Double,
    val speedKph: Double,
    val earthDistanceKm: Double,
    val marsDistanceKm: Double,
    val wikipediaUrl: String,
    val videoUrl: String,
    val details: String,
    val id: String
) {

    fun mapToRoadsterUIList(context: Context): List<RoadsterUIModel> =
        ArrayList<RoadsterUIModel>().apply {
            add(
                RoadsterMainInfoUIModel(
                    name = this@RoadsterInfoModel.name,
                    details = this@RoadsterInfoModel.details,
                    videoUrl = this@RoadsterInfoModel.videoUrl,
                    wikipediaUrl = this@RoadsterInfoModel.wikipediaUrl,
                    flickrImages = this@RoadsterInfoModel.flickrImages
                )
            )
            add(
                RoadsterCharacterUIModel(
                    R.string.roadster_launch_date,
                    this@RoadsterInfoModel.launchDate.formatDate()
                )
            )
            add(
                RoadsterCharacterUIModel(
                    R.string.roadster_orbit_type,
                    this@RoadsterInfoModel.orbitType
                )
            )
            add(
                RoadsterCharacterUIModel(
                    R.string.roadster_speed_kph,
                    context.getString(R.string.kph, this@RoadsterInfoModel.speedKph)
                )
            )
            add(
                RoadsterCharacterUIModel(
                    R.string.roadster_launch_mass,
                    context.getString(R.string.kg, this@RoadsterInfoModel.launchMassKg)
                )
            )
            add(
                RoadsterCharacterUIModel(
                    R.string.roadster_earth_distance_km,
                    context.getString(R.string.km, this@RoadsterInfoModel.earthDistanceKm)
                )
            )
            add(
                RoadsterCharacterUIModel(
                    R.string.roadster_mars_distance_km,
                    context.getString(R.string.km, this@RoadsterInfoModel.marsDistanceKm)
                )
            )
        }

}

interface RoadsterUIModel

data class RoadsterMainInfoUIModel(
    val name: String,
    val details: String,
    val videoUrl: String,
    val wikipediaUrl: String,
    val flickrImages: List<String>
) : RoadsterUIModel

data class RoadsterCharacterUIModel(
    @StringRes val name: Int,
    val value: String
) : RoadsterUIModel