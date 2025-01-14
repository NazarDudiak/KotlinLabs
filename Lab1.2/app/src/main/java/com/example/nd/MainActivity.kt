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
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FuelOilCalculatorApp()
        }
    }
}

@Composable
fun FuelOilCalculatorApp() {
    MaterialTheme {
        Scaffold { padding ->
            FuelOilCalculator(Modifier.padding(padding))
        }
    }
}

@Composable
fun FuelOilCalculator(modifier: Modifier = Modifier) {
    var hydrogen by remember { mutableStateOf("") }
    var carbon by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var moisture by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
    var vanadium by remember { mutableStateOf("") }
    var lowerHeatingValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Fuel Oil Calculator", style = MaterialTheme.typography.titleLarge)

            // Input fields
            listOf(
                "H% (Hydrogen)" to hydrogen,
                "C% (Carbon)" to carbon,
                "S% (Sulfur)" to sulfur,
                "O% (Oxygen)" to oxygen,
                "W% (Moisture)" to moisture,
                "A% (Ash)" to ash,
                "V (Vanadium mg/kg)" to vanadium,
                "Q (Lower Heating Value MJ/kg)" to lowerHeatingValue
            ).forEach { (label, state) ->
                OutlinedTextField(
                    value = state,
                    onValueChange = { newValue ->
                        when (label) {
                            "H% (Hydrogen)" -> hydrogen = newValue
                            "C% (Carbon)" -> carbon = newValue
                            "S% (Sulfur)" -> sulfur = newValue
                            "O% (Oxygen)" -> oxygen = newValue
                            "W% (Moisture)" -> moisture = newValue
                            "A% (Ash)" -> ash = newValue
                            "V (Vanadium mg/kg)" -> vanadium = newValue
                            "Q (Lower Heating Value MJ/kg)" -> lowerHeatingValue = newValue
                        }
                    },
                    label = { Text(label) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Calculate button
            Button(onClick = {
                result = calculateFuelOil(
                    hydrogen.toDoubleOrNull() ?: 0.0,
                    carbon.toDoubleOrNull() ?: 0.0,
                    sulfur.toDoubleOrNull() ?: 0.0,
                    oxygen.toDoubleOrNull() ?: 0.0,
                    moisture.toDoubleOrNull() ?: 0.0,
                    ash.toDoubleOrNull() ?: 0.0,
                    vanadium.toDoubleOrNull() ?: 0.0,
                    lowerHeatingValue.toDoubleOrNull() ?: 0.0
                )
            }) {
                Text("Calculate")
            }

            // Result display
            Text(result, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun calculateFuelOil(
    hydrogenCombustible: Double,
    carbonCombustible: Double,
    sulfurCombustible: Double,
    oxygenCombustible: Double,
    moistureCombustible: Double,
    ashCombustible: Double,
    vanadiumCombustible: Double,
    lowerHeatingValueCombustible: Double
): String {
    val hydrogenRaw = hydrogenCombustible * (100 - moistureCombustible - ashCombustible) / 100
    val carbonRaw = carbonCombustible * (100 - moistureCombustible - ashCombustible) / 100
    val sulfurRaw = sulfurCombustible * (100 - moistureCombustible - ashCombustible) / 100
    val oxygenRaw = oxygenCombustible * (100 - moistureCombustible - ashCombustible) / 100

    return """
        Calculations:
        Hydrogen (raw): ${hydrogenRaw.toFixed(2)}%
        Carbon (raw): ${carbonRaw.toFixed(2)}%
        Sulfur (raw): ${sulfurRaw.toFixed(2)}%
        Oxygen (raw): ${oxygenRaw.toFixed(2)}%
        Vanadium: ${vanadiumCombustible} mg/kg
        Lower Heating Value: ${lowerHeatingValueCombustible} MJ/kg
    """.trimIndent()
}

fun Double.toFixed(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true)
@Composable
fun FuelOilCalculatorPreview() {
    FuelOilCalculatorApp()
}
