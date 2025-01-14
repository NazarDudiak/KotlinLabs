package com.example.nd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
            LoadCalculatorApp()
        }
    }
}

@Composable
fun LoadCalculatorApp() {
    MaterialTheme {
        Scaffold { padding ->
            ElectricalLoadCalculator(Modifier.padding(padding))
        }
    }
}

@Composable
fun ElectricalLoadCalculator(modifier: Modifier = Modifier) {
    var power by remember { mutableStateOf("") }
    var efficiency by remember { mutableStateOf("") }
    var cosPhi by remember { mutableStateOf("") }
    var voltage by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var usageFactor by remember { mutableStateOf("") }
    var reactivePowerFactor by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxSize() // Заповнює весь екран
            .verticalScroll(scrollState) // Додає вертикальну прокрутку
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Electrical Load Calculator", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = power,
                onValueChange = { power = it },
                label = { Text("Номінальна потужність ЕП (Pн, кВт)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = efficiency,
                onValueChange = { efficiency = it },
                label = { Text("Коефіцієнт корисної дії (ηн)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cosPhi,
                onValueChange = { cosPhi = it },
                label = { Text("Коефіцієнт потужності (cos φ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = voltage,
                onValueChange = { voltage = it },
                label = { Text("Напруга навантаження (Uн, кВ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Кількість ЕП (n, шт)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = usageFactor,
                onValueChange = { usageFactor = it },
                label = { Text("Коефіцієнт використання (КВ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = reactivePowerFactor,
                onValueChange = { reactivePowerFactor = it },
                label = { Text("Коефіцієнт реактивної потужності (tgφ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                result = calculateLoad(
                    power.toDoubleOrNull() ?: 0.0,
                    efficiency.toDoubleOrNull() ?: 0.0,
                    cosPhi.toDoubleOrNull() ?: 0.0,
                    voltage.toDoubleOrNull() ?: 0.0,
                    quantity.toIntOrNull() ?: 0,
                    usageFactor.toDoubleOrNull() ?: 0.0,
                    reactivePowerFactor.toDoubleOrNull() ?: 0.0
                )
            }) {
                Text("Розрахувати навантаження")
            }

            Text(result, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun calculateLoad(
    power: Double,
    efficiency: Double,
    cosPhi: Double,
    voltage: Double,
    quantity: Int,
    usageFactor: Double,
    reactivePowerFactor: Double
): String {
    if (power <= 0 || efficiency <= 0 || cosPhi <= 0 || voltage <= 0 || quantity <= 0 || usageFactor <= 0 || reactivePowerFactor <= 0) {
        return "Будь ласка, введіть коректні значення."
    }

    val totalPower = power * quantity
    val activeLoad = totalPower * usageFactor
    val calculatedActiveLoad = activeLoad / (efficiency * cosPhi)
    val calculatedCurrent = (calculatedActiveLoad * 1000) / (voltage * Math.sqrt(3.0))
    val reactiveLoad = activeLoad * reactivePowerFactor
    val fullPower = Math.sqrt(activeLoad * activeLoad + reactiveLoad * reactiveLoad)

    return """
        Загальна номінальна потужність: ${totalPower.toFixed(2)} кВт
        Розрахункове активне навантаження: ${calculatedActiveLoad.toFixed(2)} кВт
        Розрахунковий струм: ${calculatedCurrent.toFixed(2)} А
        Розрахункове реактивне навантаження: ${reactiveLoad.toFixed(2)} квар
        Повна потужність: ${fullPower.toFixed(2)} кВА
    """.trimIndent()
}

fun Double.toFixed(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true)
@Composable
fun LoadCalculatorPreview() {
    LoadCalculatorApp()
}
