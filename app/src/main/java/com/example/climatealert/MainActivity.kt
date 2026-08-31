package com.example.climatealert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.climatealert.presentation.navigation.NavGraph
import com.example.climatealert.presentation.ui.theme.ClimateAlertTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClimateAlertTheme {
                NavGraph()
            }
        }
    }
}