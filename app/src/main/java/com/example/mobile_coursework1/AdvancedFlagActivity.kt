package com.example.mobile_coursework1
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject

class AdvancedLevelSectionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvancedLevelSection()
        }
    }
}

@Composable
fun AdvancedLevelSection() {
    val jsonString = readJsonFile()
    val countryList = remember { mutableListOf<CountryJson>() }

    val jsonObject = JSONObject(jsonString)
    jsonObject.keys().forEach { key ->
        val countryName = jsonObject.getString(key)
        val countryJson = CountryJson(key, countryName)
        countryList.add(countryJson)
    }

    val displayedCountries = remember { mutableListOf<CountryJson>() }

    var attemptsRemaining by rememberSaveable { mutableStateOf(3) }
    var nextButtonVisible by rememberSaveable { mutableStateOf(false) }

    var userScore by rememberSaveable { mutableStateOf(0) }

    val scoredCountries = rememberSaveable { mutableStateOf(mutableListOf<CountryJson>()) }

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

    var randomCountries by rememberSaveable {
        mutableStateOf(getNewRandomCountries())
    }

    var isCorrect1 by rememberSaveable { mutableStateOf(false) }
    var isCorrect2 by rememberSaveable { mutableStateOf(false) }
    var isCorrect3 by rememberSaveable { mutableStateOf(false) }

    var guessedCountry1 by rememberSaveable { mutableStateOf("") }
    var guessedCountry2 by rememberSaveable { mutableStateOf("") }
    var guessedCountry3 by rememberSaveable { mutableStateOf("") }

    var isAllCorrect by rememberSaveable {
        mutableStateOf(false)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        item {
            Text(
                text = "Score: $userScore",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        items(randomCountries.size) { index ->

            val country = randomCountries[index]
            //Text(text = country.countryName)
            val isEditable = when (index) {
                0 -> !isCorrect1
                1 -> !isCorrect2
                2 -> !isCorrect3
                else -> true
            }
            CountryGuessItem(
                selectedCountry = country,
                isEditable = isEditable && attemptsRemaining > 0,
                isCorrect = when (index) {
                    0 -> isCorrect1
                    1 -> isCorrect2
                    2 -> isCorrect3
                    else -> false
                },
                guessedCountry = { value ->
                    when (index) {
                        0 -> guessedCountry1 = value
                        1 -> guessedCountry2 = value
                        2 -> guessedCountry3 = value
                    }
                }
            )

        }

        item {
            Spacer(modifier = Modifier.height(20.dp))

            if (nextButtonVisible) {
                Button(
                    onClick = {
                        attemptsRemaining = 3
                        randomCountries = getNewRandomCountries()
                        nextButtonVisible = false
                        scoredCountries.value.clear()

                        isCorrect1 = false
                        isCorrect2 = false
                        isCorrect3 = false

                        guessedCountry1=""
                        guessedCountry2=""
                        guessedCountry3=""

                        userScore = 0
                    }
                ) {
                    Text(text = "Next")
                }
            } else {
                Button(
                    onClick = {
                        isCorrect1 = guessedCountry1.equals(randomCountries[0].countryName, ignoreCase = true)
                        isCorrect2 = guessedCountry2.equals(randomCountries[1].countryName, ignoreCase = true)
                        isCorrect3 = guessedCountry3.equals(randomCountries[2].countryName, ignoreCase = true)
                        isAllCorrect = isCorrect1 && isCorrect2 && isCorrect3
                        if (!isAllCorrect) {
                            attemptsRemaining--
                        }
                        if (attemptsRemaining == 0 && !isAllCorrect) {
                            nextButtonVisible = true
                        }

                        if (isCorrect1 && !scoredCountries.value.contains(randomCountries[0])) {
                            userScore++
                            scoredCountries.value.add(randomCountries[0])
                        }
                        if (isCorrect2 && !scoredCountries.value.contains(randomCountries[1])) {
                            userScore++
                            scoredCountries.value.add(randomCountries[1])
                        }
                        if (isCorrect3 && !scoredCountries.value.contains(randomCountries[2])) {
                            userScore++
                            scoredCountries.value.add(randomCountries[2])
                        }
                    },
                    enabled = !isAllCorrect && attemptsRemaining > 0
                ) {
                    Text(text = if (attemptsRemaining == 0) "Next" else "Submit")
                }
            }

            if (attemptsRemaining == 0 && !isAllCorrect) {
                Text(
                    text = "WRONG!",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
                randomCountries.forEachIndexed { index, country ->
                    if (!country.countryName.equals(guessedCountry1, ignoreCase = true) &&
                        !country.countryName.equals(guessedCountry2, ignoreCase = true) &&
                        !country.countryName.equals(guessedCountry3, ignoreCase = true)
                    ) {
                        Text(
                            text = country.countryName,
                            color = Color.Blue,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            if (isAllCorrect) {
                Text(
                    text = "CORRECT",
                    color = Color.Green,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CountryGuessItem(
    selectedCountry: CountryJson,
    isEditable: Boolean,
    isCorrect: Boolean,
    guessedCountry: (String) -> Unit
) {
    var guessedCountryValue by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val letterCode = selectedCountry.letterCode

        val image = painterResource(id = FlagUtils.getResourceId(countryCode = letterCode.lowercase()))

        Box(
            modifier = Modifier
                .size(150.dp)
                .padding(16.dp)
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = image,
                contentDescription = "Flag Icon"
            )
        }

        TextField(
            value = guessedCountryValue,
            onValueChange = {
                guessedCountryValue = it
                guessedCountry(it)
            },
            label = { Text("Guess Country") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier,
            enabled = isEditable,
            textStyle = LocalTextStyle.current.copy(color = if (isEditable) Color.Black else if (isCorrect) Color.Green else Color.Unspecified)
        )
    }
}