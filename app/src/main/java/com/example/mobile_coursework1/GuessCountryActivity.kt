package com.example.mobile_coursework1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.json.JSONObject

class GuessCountryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


                    GuessCountry()

        }
    }


}


@Composable
fun GuessCountry(

) {
    val jsonString = readJsonFile();
    val countryList = mutableListOf<CountryJson>()

    val jsonObject = JSONObject(jsonString)
    jsonObject.keys().forEach { key ->
        val countryName = jsonObject.getString(key)
        val countryJson = CountryJson(key, countryName)
        countryList.add(countryJson)
    }

    var randomCountry by remember { mutableStateOf(FlagUtils.getRandomCountry(countryList)) }
    var selectedCountry by remember { mutableStateOf<CountryJson?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var correctCountryName by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 25.dp, horizontal = 60.dp)
            .verticalScroll(rememberScrollState())

    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 200.dp)
                .shadow(elevation = 8.dp, shape = RectangleShape)
                .padding(50.dp)
                .background(Color.Yellow)

        ) {
            val image = painterResource(id = FlagUtils.getResourceId(randomCountry.letterCode.lowercase()))
            Image(
                painter = image,
                contentDescription = "Flag Icon",
                modifier = Modifier
                    .size(200.dp)
                    .verticalScroll(rememberScrollState())
            )

        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)


        ) {


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
                        randomCountry = FlagUtils.getRandomCountry(countryList)
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
    Box(



    ){
        Column(
            modifier=Modifier.padding(top = 300.dp)
                .padding(65.dp)
        ) {
            ExposedDropdownMenu(countryList, selectedCountry) { country ->
                selectedCountry = country
            }
        }

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