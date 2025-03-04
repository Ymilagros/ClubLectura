package com.ymilagros.clublectura

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") { MainScreen(navController) }
        composable("inscriptionScreen") { InscriptionScreen(navController) }
        composable("confirmationScreen/{name}/{genre}/{subscribe}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val genre = backStackEntry.arguments?.getString("genre") ?: ""
            val subscribe = if (backStackEntry.arguments?.getString("subscribe") == "true") "Sí" else "No"
            ConfirmationScreen(name, genre, subscribe)
        }
    }
}

@Composable
fun FondoDegradado(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFD1DC), Color(0xFFB5EAD7)) // Rosa pastel a verde pastel
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun MainScreen(navController: NavController) {
    FondoDegradado {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.books), contentDescription = "Libros", modifier = Modifier.size(200.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Bienvenido al Club de Lectura", fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("inscriptionScreen") }, shape = RoundedCornerShape(8.dp)) {
                Text("Inscribirse")
            }
        }
    }
}

@Composable
fun InscriptionScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Novela") }
    var subscribe by remember { mutableStateOf(false) }
    val context = LocalContext.current

    FondoDegradado {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Formulario de Inscripción", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
            Spacer(modifier = Modifier.height(8.dp))
            Text("Selecciona tu género favorito:")
            listOf("Novela", "Poesía", "Ciencia Ficción").forEach { genre ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedGenre == genre, onClick = { selectedGenre = genre })
                    Text(genre)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = subscribe, onCheckedChange = { subscribe = it })
                Text("Recibir recomendaciones")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (name.isNotBlank()) {
                    navController.navigate("confirmationScreen/$name/$selectedGenre/${subscribe}")
                } else {
                    Toast.makeText(context, "Por favor, ingresa tu nombre", Toast.LENGTH_SHORT).show()
                }
            }, shape = RoundedCornerShape(8.dp)) {
                Text("Confirmar")
            }
        }
    }
}

@Composable
fun ConfirmationScreen(name: String, genre: String, subscribe: String) {
    val context = LocalContext.current
    FondoDegradado {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido, $name!", fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tu género favorito es: $genre")
            Text("Recibirás recomendaciones: $subscribe")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { (context as? ComponentActivity)?.finish() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Salir", color = Color.White)
            }
        }
    }
}
