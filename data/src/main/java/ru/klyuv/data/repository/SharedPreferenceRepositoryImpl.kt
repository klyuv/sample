package ru.klyuv.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.klyuv.core.model.CurrentTheme
import ru.klyuv.core.repository.SharedPreferenceRepository
import ru.klyuv.data.db.DataBaseConst
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceRepositoryImpl
@Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SharedPreferenceRepository {


    override fun getCurrentTheme(): Int =
        sharedPreferences.getInt(DataBaseConst.THEME, CurrentTheme.DEFAULT.ordinal)

    override fun setTheme(id: Int) {
        sharedPreferences.edit {
            putInt(DataBaseConst.THEME, id)
        }
    }
}