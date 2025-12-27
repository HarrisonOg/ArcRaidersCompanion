package com.harrisonog.arcraiderscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.harrisonog.arcraiderscompanion.ui.navigation.NavGraph
import com.harrisonog.arcraiderscompanion.ui.theme.ArcRaidersCompanionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArcRaidersCompanionTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}