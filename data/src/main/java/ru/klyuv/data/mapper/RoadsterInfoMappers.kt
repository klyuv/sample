package ru.klyuv.data.mapper

import ru.klyuv.core.model.RoadsterInfoModel
import ru.klyuv.data.model.RoadsterInfoNetworkModel

fun RoadsterInfoNetworkModel?.mapToRoadsterInfoModel(): RoadsterInfoModel =
    RoadsterInfoModel(
        flickrImages = this?.flickrImages ?: emptyList(),
        name = this?.name ?: "",
        launchDate = this?.launchDate ?: "",
        launchMassKg = this?.launchMassKg ?: Short.MIN_VALUE,
        epochJd = this?.epochJd ?: Double.MIN_VALUE,
        orbitType = this?.orbitType ?: "",
        apoapsisAu = this?.apoapsisAu ?: Double.MIN_VALUE,
        periapsisAu = this?.periapsisAu ?: Double.MIN_VALUE,
        semiMajorAxisAu = this?.semiMajorAxisAu ?: Double.MIN_VALUE,
        eccentricity = this?.eccentricity ?: Double.MIN_VALUE,
        inclination = this?.inclination ?: Double.MIN_VALUE,
        longitude = this?.longitude ?: Double.MIN_VALUE,
        periapsisArg = this?.periapsisArg ?: Double.MIN_VALUE,
        periodDays = this?.periodDays ?: Double.MIN_VALUE,
        speedKph = this?.speedKph ?: Double.MIN_VALUE,
        earthDistanceKm = this?.earthDistanceKm ?: Double.MIN_VALUE,
        marsDistanceKm = this?.marsDistanceKm ?: Double.MIN_VALUE,
        wikipediaUrl = this?.wikipediaUrl ?: "",
        videoUrl = this?.videoUrl ?: "",
        details = this?.details ?: "",
        id = this?.id ?: ""
    )