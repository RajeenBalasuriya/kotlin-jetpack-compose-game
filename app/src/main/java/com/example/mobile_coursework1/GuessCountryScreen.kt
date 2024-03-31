package com.example.mobile_coursework1


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun GuessCountry(){
        //reading values from the json file 
        val jsonString=readJsonFile()



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

