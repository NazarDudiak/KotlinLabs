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
            FuelCalculatorApp()
        }
    }
}

@Composable
fun FuelCalculatorApp() {
    MaterialTheme {
        Scaffold { padding ->
            FuelCalculator(Modifier.padding(padding))
        }
    }
}

@Composable
fun FuelCalculator(modifier: Modifier = Modifier) {
    var hydrogen by remember { mutableStateOf("") }
    var carbon by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var nitrogen by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var moisture by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
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
        ) {
            Text("Fuel Calculator", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = hydrogen,
                onValueChange = { hydrogen = it },
                label = { Text("H%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = carbon,
                onValueChange = { carbon = it },
                label = { Text("C%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sulfur,
                onValueChange = { sulfur = it },
                label = { Text("S%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nitrogen,
                onValueChange = { nitrogen = it },
                label = { Text("N%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = oxygen,
                onValueChange = { oxygen = it },
                label = { Text("O%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = moisture,
                onValueChange = { moisture = it },
                label = { Text("W% (Moisture)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ash,
                onValueChange = { ash = it },
                label = { Text("A% (Ash)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                result = calculateFuel(
                    hydrogen.toDoubleOrNull() ?: 0.0,
                    carbon.toDoubleOrNull() ?: 0.0,
                    sulfur.toDoubleOrNull() ?: 0.0,
                    nitrogen.toDoubleOrNull() ?: 0.0,
                    oxygen.toDoubleOrNull() ?: 0.0,
                    moisture.toDoubleOrNull() ?: 0.0,
                    ash.toDoubleOrNull() ?: 0.0
                )
            }) {
                Text("Calculate")
            }

            Text(result, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


fun calculateFuel(
    hydrogenRaw: Double,
    carbonRaw: Double,
    sulfurRaw: Double,
    nitrogenRaw: Double,
    oxygenRaw: Double,
    moistureRaw: Double,
    ashRaw: Double
): String {
    val coefficientTransitionToDry = 100 / (100 - moistureRaw)
    val coefficientTransitionToCombustible = 100 / (100 - moistureRaw - ashRaw)

    val hydrogenDry = hydrogenRaw * coefficientTransitionToDry
    val carbonDry = carbonRaw * coefficientTransitionToDry
    val sulfurDry = sulfurRaw * coefficientTransitionToDry
    val nitrogenDry = nitrogenRaw * coefficientTransitionToDry
    val oxygenDry = oxygenRaw * coefficientTransitionToDry
    val ashDry = ashRaw * coefficientTransitionToDry

    val testDry = hydrogenDry + carbonDry + sulfurDry + nitrogenDry + oxygenDry + ashDry

    val hydrogenCombustible = hydrogenRaw * coefficientTransitionToCombustible
    val carbonCombustible = carbonRaw * coefficientTransitionToCombustible
    val sulfurCombustible = sulfurRaw * coefficientTransitionToCombustible
    val nitrogenCombustible = nitrogenRaw * coefficientTransitionToCombustible
    val oxygenCombustible = oxygenRaw * coefficientTransitionToCombustible

    val testCombustible = hydrogenCombustible + carbonCombustible + sulfurCombustible + nitrogenCombustible + oxygenCombustible

    val lowerHeatingValue = (339 * carbonRaw + 1030 * hydrogenRaw - 108.8 * (oxygenRaw - sulfurRaw) - 25 * moistureRaw) / 1000
    val lowerHeatingValueDry = (lowerHeatingValue + 0.025 * moistureRaw) * (100 / (100 - moistureRaw))
    val lowerHeatingValueCombustible = (lowerHeatingValue + 0.025 * moistureRaw) * (100 / (100 - moistureRaw - ashRaw))

    return if (abs(testDry - 100) <= 1 && abs(testCombustible - 100) <= 1) {
        """
            Information:
            Coefficient of transition to dry mass: ${coefficientTransitionToDry.toFixed(3)}
            Coefficient of transition to combustible mass: ${coefficientTransitionToCombustible.toFixed(3)}
            Dry mass composition: H=${hydrogenDry.toFixed(3)}%, C=${carbonDry.toFixed(3)}%, S=${sulfurDry.toFixed(3)}%, N=${nitrogenDry.toFixed(3)}%, O=${oxygenDry.toFixed(3)}%, A=${ashDry.toFixed(3)}%
            TestDry: $testDry%
            Combustible mass composition: H=${hydrogenCombustible.toFixed(3)}%, C=${carbonCombustible.toFixed(3)}%, S=${sulfurCombustible.toFixed(3)}%, N=${nitrogenCombustible.toFixed(3)}%, O=${oxygenCombustible.toFixed(3)}%
            TestCombustible: $testCombustible%
            Lower heating value: ${lowerHeatingValue.toFixed(3)} MJ/kg
            Lower heating value for dry matter: ${lowerHeatingValueDry.toFixed(3)} MJ/kg
            Lower heating value for combustible matter: ${lowerHeatingValueCombustible.toFixed(3)} MJ/kg
        """.trimIndent()
    } else if (hydrogenRaw == 0.0 || carbonRaw == 0.0 || sulfurRaw == 0.0 || nitrogenRaw == 0.0 || oxygenRaw == 0.0 || moistureRaw == 0.0 || ashRaw == 0.0) {
        "Please enter all components."
    } else {
        "Incorrect or inaccurate components. TestDry: ${testDry.toFixed(3)}% TestCombustible: ${testCombustible.toFixed(3)}%. Please try again."
    }
}

fun Double.toFixed(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true)
@Composable
fun FuelCalculatorPreview() {
    FuelCalculatorApp()
}
