package com.android.waterreminder.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import java.util.Locale

object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, "uz")
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        updateResources(context, language)
        updateResourcesLegacy(context, language)
        return context
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val preferences = context.getSharedPreferences(SELECTED_LANGUAGE, Context.MODE_PRIVATE)
        val st = preferences.getString(SELECTED_LANGUAGE, defaultLanguage)!!
        return st
    }

    private fun persist(context: Context, language: String?) {
        val preferences = context.getSharedPreferences(SELECTED_LANGUAGE, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}