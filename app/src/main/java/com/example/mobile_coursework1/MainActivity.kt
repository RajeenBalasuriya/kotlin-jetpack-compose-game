package com.example.mobile_coursework1

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_coursework1.ui.theme.Mobilecoursework1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController=rememberNavController()

            NavHost(navController = navController, startDestination = "home" ){

                //All navigation's are set below
                composable("home"){
                    HomeScreen(
                        navigateToGuessTheCountry = {
                            navController.navigate("guessTheCountry")
                        }
                    )
                }

                composable("guessTheCountry"){
                    GuessCountry()
                }
            }


        }
    }
}

