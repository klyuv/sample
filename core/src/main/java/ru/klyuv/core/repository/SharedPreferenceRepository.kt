package ru.klyuv.core.repository

interface SharedPreferenceRepository {

    fun getCurrentTheme(): Int

    fun setTheme(id: Int)

}