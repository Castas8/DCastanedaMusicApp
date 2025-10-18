package com.example.dcastaedamusicapp // Asegúrate de que sea tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dcastaedamusicapp.ui.DetailScreen
import com.example.dcastaedamusicapp.ui.HomeScreen
import com.example.dcastaedamusicapp.ui.MusicViewModel
import com.example.dcastaedamusicapp.ui.theme.DCastañedaMusicAppTheme

class MainActivity : ComponentActivity() {


    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DCastañedaMusicAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {

                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onAlbumClick = { albumId ->
                                navController.navigate("detail/$albumId")
                            }
                        )
                    }

                    composable("detail/{albumId}") { backStackEntry ->
                        val albumId = backStackEntry.arguments?.getString("albumId")
                        requireNotNull(albumId) { "El ID del álbum no puede ser nulo" }

                        DetailScreen(
                            viewModel = viewModel,
                            albumId = albumId,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}


