package com.example.nd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.pow

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
            FuelCalculators(Modifier.padding(padding))
        }
    }
}

@Composable
fun FuelCalculators(modifier: Modifier = Modifier) {
    var coalMass by remember { mutableStateOf("") }
    var fuelOilMass by remember { mutableStateOf("") }
    var gasMass by remember { mutableStateOf("") }

    var coalResult by remember { mutableStateOf("") }
    var fuelOilResult by remember { mutableStateOf("") }
    var gasResult by remember { mutableStateOf("") }

    val efficiencyCleaningGases = 0.985

    val coalLowerWorkingHeat = 20.47
    val coalFlyAsh = 0.8
    val coalContentAsh = 25.2
    val coalCombustibleSubstances = 1.5

    val fuelOilLowerWorkingHeat = 39.48
    val fuelOilFlyAsh = 1.0
    val fuelOilContentAsh = 0.15
    val fuelOilCombustibleSubstances = 0.0

    val gasLowerWorkingHeat = 33.08
    val gasFlyAsh = 0.0
    val gasContentAsh = 0.0
    val gasCombustibleSubstances = 0.0

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Coal Calculator
        Text("Coal Calculator", style = MaterialTheme.typography.titleLarge)
        BasicTextField(
            value = coalMass,
            onValueChange = { coalMass = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Button(onClick = {
            coalResult = calculateCoal(
                coalMass.toDoubleOrNull() ?: 0.0,
                coalLowerWorkingHeat,
                coalFlyAsh,
                coalContentAsh,
                coalCombustibleSubstances,
                efficiencyCleaningGases
            )
        }) {
            Text("Calculate Coal")
        }
        Text(coalResult, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Fuel Oil Calculator
        Text("Fuel Oil Calculator", style = MaterialTheme.typography.titleLarge)
        BasicTextField(
            value = fuelOilMass,
            onValueChange = { fuelOilMass = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Button(onClick = {
            fuelOilResult = calculateFuelOil(
                fuelOilMass.toDoubleOrNull() ?: 0.0,
                fuelOilLowerWorkingHeat,
                fuelOilFlyAsh,
                fuelOilContentAsh,
                fuelOilCombustibleSubstances,
                efficiencyCleaningGases
            )
        }) {
            Text("Calculate Fuel Oil")
        }
        Text(fuelOilResult, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Gas Calculator
        Text("Gas Calculator", style = MaterialTheme.typography.titleLarge)
        BasicTextField(
            value = gasMass,
            onValueChange = { gasMass = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Button(onClick = {
            gasResult = calculateGas(
                gasMass.toDoubleOrNull() ?: 0.0,
                gasLowerWorkingHeat,
                gasFlyAsh,
                gasContentAsh,
                gasCombustibleSubstances,
                efficiencyCleaningGases
            )
        }) {
            Text("Calculate Gas")
        }
        Text(gasResult, style = MaterialTheme.typography.bodyLarge)
    }
}

fun calculateCoal(
    coalMass: Double,
    coalLowerWorkingHeat: Double,
    coalFlyAsh: Double,
    coalContentAsh: Double,
    coalCombustibleSubstances: Double,
    efficiencyCleaningGases: Double
): String {
    val coalEmissionIndex = (10.0.pow(6) / coalLowerWorkingHeat) * coalFlyAsh *
            (coalContentAsh / (100 - coalCombustibleSubstances)) * (1 - efficiencyCleaningGases)

    val coalGrossSolidParticles = (10.0.pow(-6)) * coalEmissionIndex * coalLowerWorkingHeat * coalMass

    return """
        Coal Emission Index: ${coalEmissionIndex.toFixed(3)}
        Coal Gross Solid Particles: ${coalGrossSolidParticles.toFixed(3)}
    """.trimIndent()
}

fun calculateFuelOil(
    fuelOilMass: Double,
    fuelOilLowerWorkingHeat: Double,
    fuelOilFlyAsh: Double,
    fuelOilContentAsh: Double,
    fuelOilCombustibleSubstances: Double,
    efficiencyCleaningGases: Double
): String {
    val fuelOilEmissionIndex = (10.0.pow(6) / fuelOilLowerWorkingHeat) * fuelOilFlyAsh *
            (fuelOilContentAsh / (100 - fuelOilCombustibleSubstances)) * (1 - efficiencyCleaningGases)

    val fuelOilGrossSolidParticles = (10.0.pow(-6)) * fuelOilEmissionIndex * fuelOilLowerWorkingHeat * fuelOilMass

    return """
        Fuel Oil Emission Index: ${fuelOilEmissionIndex.toFixed(3)}
        Fuel Oil Gross Solid Particles: ${fuelOilGrossSolidParticles.toFixed(3)}
    """.trimIndent()
}

fun calculateGas(
    gasMass: Double,
    gasLowerWorkingHeat: Double,
    gasFlyAsh: Double,
    gasContentAsh: Double,
    gasCombustibleSubstances: Double,
    efficiencyCleaningGases: Double
): String {
    // If both fly ash and content ash are zero, set to a small value or skip them
    val adjustedGasFlyAsh = if (gasFlyAsh == 0.0) 0.01 else gasFlyAsh
    val adjustedGasContentAsh = if (gasContentAsh == 0.0) 0.01 else gasContentAsh

    // Calculate the gas emission index considering the adjusted ash values
    val gasEmissionIndex = (10.0.pow(6) / gasLowerWorkingHeat) * adjustedGasFlyAsh *
            (adjustedGasContentAsh / (100 - gasCombustibleSubstances)) * (1 - efficiencyCleaningGases)

    // Calculate gas gross solid particles
    val gasGrossSolidParticles = (10.0.pow(-6)) * gasEmissionIndex * gasLowerWorkingHeat * gasMass

    return """
        Gas Emission Index: ${gasEmissionIndex.toFixed(3)}
        Gas Gross Solid Particles: ${gasGrossSolidParticles.toFixed(3)}
    """.trimIndent()
}



fun Double.toFixed(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true)
@Composable
fun FuelCalculatorPreview() {
    FuelCalculatorApp()
}