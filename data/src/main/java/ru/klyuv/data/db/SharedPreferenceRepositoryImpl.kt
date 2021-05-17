package ru.klyuv.data.db

import android.content.SharedPreferences
import ru.klyuv.core.repository.SharedPreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceRepositoryImpl
@Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SharedPreferenceRepository {



}