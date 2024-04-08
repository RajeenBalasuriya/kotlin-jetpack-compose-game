package com.example.mobile_coursework1
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject


class GuessFlagActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagSection()
        }
    }
}


@Composable
fun FlagSection() {
    val jsonString = readJsonFile()
    val countryList = mutableListOf<CountryJson>()

    val jsonObject = JSONObject(jsonString)
    jsonObject.keys().forEach { key ->
        val countryName = jsonObject.getString(key)
        val countryJson = CountryJson(key, countryName)
        countryList.add(countryJson)
    }

    var displayedCountries by remember { mutableStateOf(mutableListOf<CountryJson>()) }

    // Function to get three new random countries
    fun getNewRandomCountries(): List<CountryJson> {
        val newCountries = mutableListOf<CountryJson>()
        val allCountries = countryList.filter { !displayedCountries.contains(it) }
        repeat(3) {
            val newCountry = FlagUtils.getRandomCountry(allCountries)
            newCountries.add(newCountry)
            displayedCountries.add(newCountry)
        }
        return newCountries
    }

    var randomCountries by remember {
        mutableStateOf(getNewRandomCountries())
    }

    var countryToGuess by remember {
        mutableStateOf(randomCountries.random().countryName)
    }

    var isCorrect by remember { mutableStateOf(false) }
    var flagClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Guess flag of the country :  ${countryToGuess}",
                style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            randomCountries.forEach { country ->
                FlagItem(
                    selectedCountry = country,
                    onItemClick = { clickedCountry ->
                        if (clickedCountry.countryName == countryToGuess) {
                            isCorrect = true
                        } else {
                            isCorrect = false
                        }
                        flagClicked = true
                    },
                    countryToGuess = countryToGuess,
                    showCountryToGuess = flagClicked
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (flagClicked) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isCorrect) {
                    Text(
                        text = "CORRECT!",
                        color = Color.Green,
                        modifier = Modifier.padding(top = 150.dp)
                    )
                } else {
                    Text(
                        text = "WRONG!",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 150.dp)
                    )
                }
                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = {
                        // Reset the flag
                        isCorrect = false
                        flagClicked = false
                        displayedCountries.clear() 
                        randomCountries = getNewRandomCountries() // Get new random countries
                        countryToGuess = randomCountries.random().countryName
                    }
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Composable
fun FlagItem(
    selectedCountry: CountryJson,
    onItemClick: (CountryJson) -> Unit,
    countryToGuess: String,
    showCountryToGuess: Boolean
) {
    Column {
        val letterCode = selectedCountry.letterCode

        // Calling for getResource id to get the resource id
        val image = painterResource(id = FlagUtils.getResourceId(countryCode = letterCode.lowercase()))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 16.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .shadow(4.dp)
                    .clickable {
                        onItemClick(selectedCountry)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = image,
                    contentDescription = "Flag Icon"
                )
            }

            if (showCountryToGuess && selectedCountry.countryName == countryToGuess) {
                Text(
                    text = countryToGuess,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}




