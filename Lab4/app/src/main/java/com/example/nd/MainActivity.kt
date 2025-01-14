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
            FaultCurrentCalculatorApp()
        }
    }
}

@Composable
fun FaultCurrentCalculatorApp() {
    MaterialTheme {
        Scaffold { paddingValues ->
            FaultCurrentCalculator(Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun FaultCurrentCalculator(modifier: Modifier = Modifier) {
    var voltageFault by remember { mutableStateOf("") }
    var impedance by remember { mutableStateOf("") }
    var currentType by remember { mutableStateOf("threePhase") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Розрахунок струмів короткого замикання", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = voltageFault,
            onValueChange = { voltageFault = it },
            label = { Text("Напруга (U, кВ)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = impedance,
            onValueChange = { impedance = it },
            label = { Text("Імпеданс системи (Z, Ом)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Тип струму:")
            RadioButton(selected = currentType == "threePhase", onClick = { currentType = "threePhase" })
            Text("Трифазний")
            RadioButton(selected = currentType == "singlePhase", onClick = { currentType = "singlePhase" })
            Text("Однофазний")
        }

        Button(onClick = {
            result = calculateFaultCurrent(
                voltageFault.toDoubleOrNull() ?: 0.0,
                impedance.toDoubleOrNull() ?: 0.0,
                currentType
            )
        }) {
            Text("Розрахувати струм КЗ")
        }

        Text(result, style = MaterialTheme.typography.bodyLarge)
    }
}

fun calculateFaultCurrent(voltageFault: Double, impedance: Double, currentType: String): String {
    if (voltageFault <= 0 || impedance <= 0) {
        return "Будь ласка, введіть коректні значення."
    }

    val faultCurrent = when (currentType) {
        "threePhase" -> (voltageFault * 1000) / (impedance * Math.sqrt(3.0))
        "singlePhase" -> (voltageFault * 1000) / impedance
        else -> 0.0
    }

    return "Розрахунковий струм короткого замикання: ${faultCurrent.toFixed(2)} А"
}

fun Double.toFixed(digits: Int): String = "%.${digits}f".format(this)

@Preview(showBackground = true)
@Composable
fun FaultCurrentCalculatorPreview() {
    FaultCurrentCalculatorApp()
}
