package com.example.mobile_coursework1

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject

data class CountryJson(
    val letterCode: String,
    val countryName: String
)

object FlagUtils {
    fun getRandomCountry(countryList: List<CountryJson>): CountryJson {
        return countryList.random()
    }

    @Composable
    fun getResourceId(countryCode: String): Int {
        val context = LocalContext.current
        return try {
            context.resources.getIdentifier(countryCode, "drawable", context.packageName)
        } catch (e: Exception) {
            // If resource not found, return a default drawable resource
            0
        }
    }
}

@Composable
fun readJsonFile(): String {
    val context = LocalContext.current

    return try {
        context.assets
            .open("countries.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        ""
    } catch (jsonException: JSONException) {
        jsonException.printStackTrace()
        ""
    }
}
