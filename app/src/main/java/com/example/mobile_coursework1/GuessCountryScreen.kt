package com.example.mobile_coursework1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.json.JSONObject

@Composable
fun GuessCountry() {
    val jsonString = readJsonFile()
    val countryList = mutableListOf<CountryJson>()

    val jsonObject = JSONObject(jsonString)
    jsonObject.keys().forEach { key ->
        val countryName = jsonObject.getString(key)
        val countryJson = CountryJson(key, countryName)
        countryList.add(countryJson)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 25.dp, horizontal = 60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 200.dp) // Adjusted padding for space between flag and button
                .shadow(elevation = 8.dp, shape = RectangleShape)
                .padding(50.dp)
        ) {
            GenerateRandomFlag(countryList)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            DropDownMenu(countryList)
            // Added space between flag box and button
            Spacer(modifier = Modifier.height(370.dp))

            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)

            ) {
                Text(text = "Submit")
            }
        }
    }

}

@Composable
private fun GenerateRandomFlag(countryList: MutableList<CountryJson>) {
    //getting a random country from the list
    val randomCountry = countryList.random()
    //get the name and the country code
    val randomCountryName = randomCountry.countryName
    val randomCountryLetterCode = randomCountry.letterCode.lowercase()

    val image = painterResource(id = getResourceId(randomCountryLetterCode))

    Image(
        painter = image,
        contentDescription = "Flag Icon",
        modifier = Modifier.size(200.dp) // Adjust size as needed
    )
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

private fun getResourceId(countryCode: String): Int {
    // Assuming your drawable resources are named using the country codes
    return try {
        R.drawable::class.java.getField(countryCode).getInt(null)
    } catch (e: Exception) {
        // If resource not found, return a default drawable resource
        0
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownMenu(countryList: List<CountryJson>) {

        var isExpanded by remember { mutableStateOf(false) }
        var selectedCountry by remember { mutableStateOf<CountryJson?>(null) }

        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
            TextField(
                value = selectedCountry?.countryName ?: "Guess The Country In The Flag",
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                countryList.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(text = country.countryName) },
                        onClick = {
                            selectedCountry = country
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }



//data class
data class CountryJson(
    val letterCode: String,
    val countryName: String
)


