package com.smartbudget.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("smartbudget_prefs", Context.MODE_PRIVATE)

    private val _currency = MutableStateFlow(prefs.getString("currency", "MAD") ?: "MAD")
    val currency: StateFlow<String> = _currency

    private val _language = MutableStateFlow(prefs.getString("language", "French (Français)") ?: "French (Français)")
    val language: StateFlow<String> = _language

    fun setCurrency(newCurrency: String) {
        prefs.edit().putString("currency", newCurrency).apply()
        _currency.value = newCurrency
    }

    fun setLanguage(newLanguage: String) {
        prefs.edit().putString("language", newLanguage).apply()
        _language.value = newLanguage
    }
}
