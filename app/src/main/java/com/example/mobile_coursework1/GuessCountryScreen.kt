package com.example.mobile_coursework1

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
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

    var randomCountry by remember { mutableStateOf(getRandomCountry(countryList)) }
    var selectedCountry by remember { mutableStateOf<CountryJson?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var correctCountryName by remember { mutableStateOf("") }

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
            val image = painterResource(id = getResourceId(randomCountry.letterCode.lowercase()))
            Image(
                painter = image,
                contentDescription = "Flag Icon",
                modifier = Modifier.size(200.dp) // Adjust size as needed
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            ExposedDropdownMenu(countryList, selectedCountry) { country ->
                selectedCountry = country
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showResult) {
                val isCorrect = selectedCountry?.countryName == randomCountry.countryName
                val textColor = if (isCorrect) Color.Green else Color.Red
                val resultText = if (isCorrect) "CORRECT!" else "WRONG!"
                val correctCountryText = if (!isCorrect) "Correct country: $correctCountryName" else ""
                Text(
                    text = resultText,
                    color = textColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if (!isCorrect) {
                    Text(
                        text = correctCountryText,
                        color = Color.Blue,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (showResult) {
                        randomCountry = getRandomCountry(countryList)
                        selectedCountry = null
                        showResult = false
                        correctCountryName = ""
                    } else {
                        showResult = true
                        if (selectedCountry?.countryName != randomCountry.countryName) {
                            correctCountryName = randomCountry.countryName
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = if (showResult) "Next" else "Submit")
            }
        }
    }
}

private fun getRandomCountry(countryList: List<CountryJson>): CountryJson {
    return countryList.random()
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
 
    return try {
        R.drawable::class.java.getField(countryCode).getInt(null)
    } catch (e: Exception) {
        // If resource not found, return a default drawable resource
        0
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

private fun ExposedDropdownMenu(
    countryList: List<CountryJson>,
    selectedCountry: CountryJson?,
    onCountrySelected: (CountryJson) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

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
                        onCountrySelected(country)
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
