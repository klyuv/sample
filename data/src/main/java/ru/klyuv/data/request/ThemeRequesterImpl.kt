package ru.klyuv.data.request

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import ru.klyuv.core.model.CurrentTheme
import ru.klyuv.core.repository.SharedPreferenceRepository
import ru.klyuv.core.requester.ThemeRequester
import javax.inject.Inject

class ThemeRequesterImpl
@Inject constructor(
    private val sharedPreferencesRepository: SharedPreferenceRepository
) : ThemeRequester {

    override fun getCurrentTheme(): CurrentTheme =
        CurrentTheme.values()[sharedPreferencesRepository.getCurrentTheme()]

    override fun switchTheme() {
        when (getCurrentTheme()) {
            CurrentTheme.DEFAULT -> switchDefaultCurrentTheme()
            CurrentTheme.DARK -> sharedPreferencesRepository.setTheme(
                CurrentTheme.LIGHT.ordinal)
            CurrentTheme.LIGHT -> sharedPreferencesRepository.setTheme(
                CurrentTheme.DARK.ordinal)
        }
        changeTheme()
    }

    private fun switchDefaultCurrentTheme() =
        when (Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> sharedPreferencesRepository.setTheme(
                CurrentTheme.DARK.ordinal)
            Configuration.UI_MODE_NIGHT_YES -> sharedPreferencesRepository.setTheme(
                CurrentTheme.LIGHT.ordinal)
            else -> sharedPreferencesRepository.setTheme(CurrentTheme.DARK.ordinal)
        }

    override fun changeTheme() {
        when (getCurrentTheme()) {
            CurrentTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            CurrentTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            CurrentTheme.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

}