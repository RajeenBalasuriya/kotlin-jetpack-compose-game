package com.example.mobile_coursework1


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.json.JSONArray
import org.json.JSONObject


@Composable
fun GuessCountry(){


        //reading values from the json file
        val jsonString=readJsonFile()

        //mutable list to store country objects
        val countryList= mutableListOf<CountryJson>()

        val jsonObject = JSONObject(jsonString)

    // Iterate through each key-value pair in the JSON object to make country objects
    jsonObject.keys().forEach { key ->
        val countryName = jsonObject.getString(key)
        val countryJson = CountryJson(key, countryName)
        countryList.add(countryJson)
    }



}

@Composable
private fun readJsonFile(): String {
    val context = LocalContext.current

    //return jsonString
    return context.assets
        .open("countries.json")
        .bufferedReader()
        .use { it.readText() }


}

//data class
data class CountryJson(
    val letterCode:String,
    val countryName:String
)

