package com.example.nd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolarProfitCalculatorApp()
        }
    }
}

@Composable
fun SolarProfitCalculatorApp() {
    MaterialTheme {
        Scaffold { paddingValues ->
            SolarProfitCalculator(Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun SolarProfitCalculator(modifier: Modifier = Modifier) {
    var power by remember { mutableStateOf("") }
    var performance by remember { mutableStateOf("") }
    var sunnyDays by remember { mutableStateOf("") }
    var tariff by remember { mutableStateOf("") }
    var efficiency by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Розрахунок прибутку від сонячних електростанцій", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = power,
            onValueChange = { power = it },
            label = { Text("Потужність станції (кВт)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = performance,
            onValueChange = { performance = it },
            label = { Text("Середня продуктивність (годин/день)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = sunnyDays,
            onValueChange = { sunnyDays = it },
            label = { Text("Кількість сонячних днів у році") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tariff,
            onValueChange = { tariff = it },
            label = { Text("Тариф на електроенергію (грн/кВт·год)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = efficiency,
            onValueChange = { efficiency = it },
            label = { Text("Ефективність системи прогнозування (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            result = calculateSolarProfit(
                power.toDoubleOrNull() ?: 0.0,
                performance.toDoubleOrNull() ?: 0.0,
                sunnyDays.toIntOrNull() ?: 0,
                tariff.toDoubleOrNull() ?: 0.0,
                efficiency.toDoubleOrNull() ?: 0.0
            )
        }) {
            Text("Розрахувати прибуток")
        }

        Text(result, style = MaterialTheme.typography.bodyLarge)
    }
}

fun calculateSolarProfit(
    power: Double,
    performance: Double,
    sunnyDays: Int,
    tariff: Double,
    efficiency: Double
): String {
    if (power <= 0 || performance <= 0 || sunnyDays <= 0 || tariff <= 0 || efficiency <= 0) {
        return "Будь ласка, введіть коректні значення."
    }

    val effectiveEfficiency = efficiency / 100
    val annualProduction = power * performance * sunnyDays * effectiveEfficiency
    val annualProfit = annualProduction * tariff

    return "Щорічний прибуток: ${annualProfit.toFixed(2)} грн"
}

fun Double.toFixed(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true)
@Composable
fun SolarProfitCalculatorPreview() {
    SolarProfitCalculatorApp()
}
