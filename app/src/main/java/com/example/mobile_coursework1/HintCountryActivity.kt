package com.example.mobile_coursework1
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import java.util.Locale


class HintCountryActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


        HintScreen(context =applicationContext )

        }
    }
    @Composable
    fun HintScreen(context: Context) {
        val jsonString = readJsonFile()
        val countryList = mutableListOf<CountryJson>()

        val jsonObject = JSONObject(jsonString)
        jsonObject.keys().forEach { key ->
            val countryName = jsonObject.getString(key)
            val countryJson = CountryJson(key, countryName)
            countryList.add(countryJson)
        }

        val randomCountry by remember { mutableStateOf(FlagUtils.getRandomCountry(countryList)) }
        var userInput by remember { mutableStateOf(TextFieldValue()) }
        var displayedText by remember { mutableStateOf(getEmptyDashedString(randomCountry.countryName)) }
        var inCorrectCount by remember { mutableIntStateOf(0) }
        var noRemainingAttempts by remember { mutableStateOf(false) }
        var correctAnswer by remember { mutableStateOf(false) }
        var showNext = false


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 25.dp, horizontal = 60.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 200.dp)
                    .shadow(elevation = 8.dp, shape = RectangleShape)
                    .padding(50.dp)
            ) {
                val image =
                    painterResource(id = FlagUtils.getResourceId(randomCountry.letterCode.lowercase()))
                Image(
                    painter = image, contentDescription = "Flag Icon", modifier = Modifier.size(200.dp)
                )
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp)
            ) {
                if (noRemainingAttempts) {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "WRONG!",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {


                        Text(
                            text = "Correct Name : ${randomCountry.countryName}",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = Color.Blue
                        )
                    }
                }

                if (correctAnswer && !noRemainingAttempts) {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CORRECT!",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            color = Color.Green
                        )

                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayedText,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                }
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("Enter text") },
                    singleLine = true,
                )

                Text("Kotlin: ${randomCountry.countryName}")

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if(showNext){
                            recreateActivity()
                        }
                        if (userInput.text.length > 1) {
                            Toast.makeText(
                                context, "Please enter only one character", Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            val userInputUpperCase = userInput.text.uppercase(Locale.ROOT)
                            val countryNameUpperCase = randomCountry.countryName.uppercase(
                                Locale.ROOT
                            )


                            val hasMatchingValues =
                                hasMatchingLetters(userInputUpperCase, countryNameUpperCase)
                            if (hasMatchingValues) {

                                displayedText = updateDisplayedText(
                                    displayedText, countryNameUpperCase, userInputUpperCase
                                )
                                correctAnswer =
                                    displayedText.uppercase(Locale.ROOT) == countryNameUpperCase
                            } else {
                                Toast.makeText(context, "Wrong input try again", Toast.LENGTH_SHORT)
                                    .show()
                                inCorrectCount++
                                noRemainingAttempts = inCorrectCount > 2

                            }
                        }
                        userInput = TextFieldValue("")
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {

                    if (inCorrectCount == 3 || correctAnswer) {
                        showNext = true
                    }
                    Text(text = if (showNext) "Next" else "Submit")
                }
            }
        }


    }

    private fun getEmptyDashedString(countryName: String?): String {
        return countryName?.let {
            "-".repeat(it.length)
        } ?: ""
    }

    private fun updateDisplayedText(
        currentText: String, selectedCountry: String, userInput: String
    ): String {
        val updatedText = StringBuilder(currentText)

        for (i in selectedCountry.indices) {
            if (selectedCountry[i].toString() == userInput) {
                updatedText.setCharAt(i, userInput[0])
            }
        }

        return updatedText.toString()
    }

    private fun hasMatchingLetters(str1: String, str2: String): Boolean {
        for (char1 in str1) {
            if (str2.contains(char1)) {
                return true
            }
        }
        return false}

    private fun recreateActivity() {
        val intent = Intent(this, HintCountryActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


}

