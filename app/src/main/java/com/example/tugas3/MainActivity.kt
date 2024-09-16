package com.example.tugas3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas3.ui.theme.Tugas3Theme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tugas3Theme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var lastOperator by remember { mutableStateOf<Char?>(null) }
    var lastValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Double?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFd4e9e3)),
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text("Calculator", color = Color.White, fontWeight = FontWeight.SemiBold)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF2BBD6))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "$lastValue ${lastOperator ?: ""} $input",
                    fontSize = if (result == null) 36.sp else 24.sp,
                    fontWeight = FontWeight.Bold
                )
                if (result != null) {
                    Text(
                        text = "= ${result!!}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Tombol kalkulator
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttonModifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .padding(4.dp)

                CalculatorColumn(
                    listOf("7", "8", "9", "C"),
                    buttonModifier,
                    Color(0xFFF2BBD6)
                ) { value -> onButtonClick(value, input, lastOperator, lastValue, { input = it }, { lastOperator = it }, { lastValue = it }, { result = it }) }

                CalculatorColumn(
                    listOf("3", "4", "5", "6"),
                    buttonModifier,
                    Color(0xFFF2BBD6)
                ) { value -> onButtonClick(value, input, lastOperator, lastValue, { input = it }, { lastOperator = it }, { lastValue = it }, { result = it }) }

                CalculatorColumn(
                    listOf("1", "2", "0", "."),
                    buttonModifier,
                    Color(0xFFF2BBD6)
                ) { value -> onButtonClick(value, input, lastOperator, lastValue, { input = it }, { lastOperator = it }, { lastValue = it }, { result = it }) }

                CalculatorColumn(
                    listOf("+", "-", "x", "/"),
                    buttonModifier,
                    Color(0xFFC20D84)
                ) { value -> onButtonClick(value, input, lastOperator, lastValue, { input = it }, { lastOperator = it }, { lastValue = it }, { result = it }) }

                Button(
                    onClick = {
                        onButtonClick("=", input, lastOperator, lastValue, { input = it }, { lastOperator = it }, { lastValue = it }, { result = it })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .height(75.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0E1D3))
                ) {
                    Text(text = "=", color = Color.Black, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CalculatorColumn(buttons: List<String>, modifier: Modifier, backgroundColor: Color, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttons.forEach { label ->
            Button(
                onClick = { onClick(label) },
                modifier = modifier
                    .background(backgroundColor, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
            ) {
                Text(text = label, color = Color.White, fontSize = 24.sp)
            }
        }
    }
}

fun onButtonClick(
    value: String,
    input: String,
    lastOperator: Char?,
    lastValue: String,
    updateInput: (String) -> Unit,
    updateLastOperator: (Char?) -> Unit,
    updateLastValue: (String) -> Unit,
    updateResult: (Double?) -> Unit
) {
    when (value) {
        "C" -> {
            updateInput("")
            updateLastValue("")
            updateLastOperator(null)
            updateResult(null)
        }
        in "0".."9", "." -> {
            if (input.isEmpty() || input == "0") {
                updateInput(value)
            } else {
                updateInput(input + value)
            }
        }
        in listOf("+", "-", "x", "/") -> {
            if (input.isNotEmpty()) {
                updateLastOperator(value.first())
                updateLastValue(input)
                updateInput("")
            }
        }
        "=" -> {
            val num1 = lastValue.toDoubleOrNull() ?: 0.0
            val num2 = input.toDoubleOrNull() ?: 0.0
            val res = when (lastOperator) {
                '+' -> num1 + num2
                '-' -> num1 - num2
                'x' -> num1 * num2
                '/' -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }
            updateResult(res)
        }
        else -> {
            updateInput("")
            updateLastValue("")
            updateLastOperator(null)
            updateResult(null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    Tugas3Theme {
        CalculatorApp()
    }
}
