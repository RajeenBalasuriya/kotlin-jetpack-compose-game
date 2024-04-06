package com.example.mobile_coursework1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = this
            val navController=rememberNavController()

            NavHost(navController = navController, startDestination = "home" ){

                //All navigation's are set below
                composable("home"){
                    HomeScreen(
                        context = context,
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

