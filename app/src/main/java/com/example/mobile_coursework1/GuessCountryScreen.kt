package com.example.mobile_coursework1


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.core.content.res.TypedArrayUtils.getResourceId
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

        GenerateRandomFlag(countryList)



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

@Composable
private fun GenerateRandomFlag(countryList: MutableList<CountryJson>){

    //getting a random country from the list
    val randomCountry=countryList.random()
    //get the name and the country code
    val randomCountryName=randomCountry.countryName
    val randomCountryLetterCode =(randomCountry.letterCode).lowercase()

    val image = painterResource(id = getResourceId(randomCountryLetterCode))

    Image(
        painter = image,
        contentDescription = "Flag Icon",
        modifier = Modifier.size(200.dp) // Adjust size as needed
    )





}
private fun getResourceId(countryCode: String): Int {
    // Assuming your drawable resources are named using the country codes
    return try {
        R.drawable::class.java.getField(countryCode).getInt(null)
    } catch (e: Exception) {
        // If resource not found, return a default drawable resource
        R.drawable.flag
    }
}

//data class
data class CountryJson(
    val letterCode:String,
    val countryName:String
)

