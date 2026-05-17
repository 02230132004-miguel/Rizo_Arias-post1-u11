package com.example.unidad11post1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.notes.NotesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "notes"
                    ) {
                        composable("notes") {
                            NotesScreen(
                                onNavigateToDetail = { id ->
                                    navController.navigate("detail/$id")
                                }
                            )
                        }
                        composable("detail/{noteId}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("noteId")
                            DetailScreen(id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(noteId: String?) {
    Text(
        text = "Detalle de la nota: $noteId", 
        modifier = Modifier.padding(16.dp)
    )
}
