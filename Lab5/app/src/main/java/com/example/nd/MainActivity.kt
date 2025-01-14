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
            ReliabilityLossCalculatorApp()
        }
    }
}

@Composable
fun ReliabilityLossCalculatorApp() {
    MaterialTheme {
        Scaffold { paddingValues ->
            ReliabilityLossCalculator(Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun ReliabilityLossCalculator(modifier: Modifier = Modifier) {
    var failureRateSingle by remember { mutableStateOf("") }
    var repairTimeSingle by remember { mutableStateOf("") }
    var failureRateDouble by remember { mutableStateOf("") }
    var repairTimeDouble by remember { mutableStateOf("") }
    var powerLoss by remember { mutableStateOf("") }
    var outageCost by remember { mutableStateOf("") }
    var reliabilityResult by remember { mutableStateOf("") }
    var lossResult by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Калькулятор надійності та збитків",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = failureRateSingle,
            onValueChange = { failureRateSingle = it },
            label = { Text("Інтенсивність відмов одноколової системи (λ, 1/год)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = repairTimeSingle,
            onValueChange = { repairTimeSingle = it },
            label = { Text("Середній час відновлення одноколової системи (t, год)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = failureRateDouble,
            onValueChange = { failureRateDouble = it },
            label = { Text("Інтенсивність відмов двоколової системи (λ, 1/год)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = repairTimeDouble,
            onValueChange = { repairTimeDouble = it },
            label = { Text("Середній час відновлення двоколової системи (t, год)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = powerLoss,
            onValueChange = { powerLoss = it },
            label = { Text("Втрати потужності при перерві (кВт)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = outageCost,
            onValueChange = { outageCost = it },
            label = { Text("Вартість втрат електропостачання (грн/кВт·год)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val (reliability, loss) = calculateReliabilityAndLoss(
                failureRateSingle.toDoubleOrNull(),
                repairTimeSingle.toDoubleOrNull(),
                failureRateDouble.toDoubleOrNull(),
                repairTimeDouble.toDoubleOrNull(),
                powerLoss.toDoubleOrNull(),
                outageCost.toDoubleOrNull()
            )
            reliabilityResult = reliability
            lossResult = loss
        }) {
            Text("Розрахувати")
        }

        Text(reliabilityResult, style = MaterialTheme.typography.bodyLarge)
        Text(lossResult, style = MaterialTheme.typography.bodyLarge)
    }
}

fun calculateReliabilityAndLoss(
    failureRateSingle: Double?,
    repairTimeSingle: Double?,
    failureRateDouble: Double?,
    repairTimeDouble: Double?,
    powerLoss: Double?,
    outageCost: Double?
): Pair<String, String> {
    if (failureRateSingle == null || repairTimeSingle == null ||
        failureRateDouble == null || repairTimeDouble == null ||
        powerLoss == null || outageCost == null
    ) {
        return Pair("Будь ласка, введіть коректні значення.", "")
    }

    // Розрахунок надійності
    val reliabilitySingle = 1 / (failureRateSingle * repairTimeSingle)
    val reliabilityDouble = 1 / (failureRateDouble * repairTimeDouble)

    // Розрахунок збитків
    val lossSingle = powerLoss * repairTimeSingle * outageCost
    val lossDouble = powerLoss * repairTimeDouble * outageCost

    val reliabilityResult = """
        Надійність одноколової системи: ${reliabilitySingle.toFixed(2)} (безвідмовні години)
        Надійність двоколової системи: ${reliabilityDouble.toFixed(2)} (безвідмовні години)
    """.trimIndent()

    val lossResult = """
        Збитки одноколової системи: ${lossSingle.toFixed(2)} грн
        Збитки двоколової системи: ${lossDouble.toFixed(2)} грн
    """.trimIndent()

    return Pair(reliabilityResult, lossResult)
}

fun Double.toFixed(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true)
@Composable
fun ReliabilityLossCalculatorPreview() {
    ReliabilityLossCalculatorApp()
}
