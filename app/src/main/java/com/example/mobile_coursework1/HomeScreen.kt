package com.example.mobile_coursework1


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(context: Context, navigateToGuessTheCountry: () -> Unit) {
    //Components of Home screen found below
    TitleSection()
    ButtonSection(context = context, navigateToGuessTheCountry = navigateToGuessTheCountry)
}

@Composable
fun TitleSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp, horizontal = 8.dp)

    ) {
        Row(
            modifier = Modifier.padding(horizontal = 100.dp, vertical = 20.dp)
        ) {
            Text(
                text = "LETS PLAY !",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            // Add space between text and image
            Spacer(modifier = Modifier.width(8.dp))
            // Load the image from drawable folder
            val image = painterResource(id = R.drawable.flag)
            Image(
                painter = image,
                contentDescription = "Flag Icon",
                modifier = Modifier.size(50.dp) // Adjust size as needed
            )
        }


    }
}
@Composable
fun ButtonSection(context: Context, navigateToGuessTheCountry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        // Change the text for each button
        ButtonItem(text = "Guess the Country", onClick= {
            val intent = Intent(context, GuessCountryActivity::class.java)
            context.startActivity(intent)
        })
        ButtonItem(text = "Guess-Hints", onClick =navigateToGuessTheCountry)
        ButtonItem(text = "Guess the Flag",onClick ={
            val intent = Intent(context, GuessFlagActivity::class.java)
            context.startActivity(intent)
        })
        ButtonItem(text = "Advanced Level",onClick =navigateToGuessTheCountry)
    }
}

@Composable
fun ButtonItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth(0.9f)
                .height(40.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        ) {
            androidx.compose.material3.Text(
                text = text,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}
