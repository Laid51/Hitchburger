package fr.isen.oussenilaid.hitchburger

import LoadOrdersScreen
import OrderDetails
import OrderMessage
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.oussenilaid.hitchburger.ui.theme.HitchBurgerTheme
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HitchBurgerTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("HitchBurger") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LogoAndDescription()
            OrderForm()
            LoadOrdersScreen(userId = 353)
        }
    }
}


@Composable
fun LogoAndDescription() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.hitchb),
            contentDescription = "Logo HitchBurger",
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = "Bienvenue à HitchBurger! Commandez votre burger préféré.",
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}


@Composable
fun Greeting(name: String, burgerList: List<String>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedBurger by remember { mutableStateOf(burgerList.first()) }

    Column(modifier = modifier) {
        Text(
            text = "Hello $name! Click here to choose a burger: $selectedBurger",
            modifier = Modifier.clickable { expanded = true },
            fontSize = 18.sp
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            burgerList.forEach { burger ->
                DropdownMenuItem(
                    onClick = {
                        selectedBurger = burger
                        expanded = false
                    }
                ) {
                    Text(text = burger)
                }
            }
        }
    }
}





@Composable
fun TimePickerExample() {
    val context = LocalContext.current
    var time by remember { mutableStateOf("Select Time") }

    Button(onClick = {
        val timePicker = TimePickerDialog(context, { _, hour: Int, minute: Int ->
            time = "$hour:$minute"
        }, 12, 0, true)
        timePicker.show()
    }) {
        Text(text = time)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HitchBurgerTheme {
        LogoAndDescription()
        Greeting("Android", listOf("Burger du chef", "Cheese Burger", "Burger Montagnard", "Burger Italien", "Burger Végétarien"))
        TimePickerExample()
    }
}


@Composable
fun OrderForm() {
    val coroutineScope = rememberCoroutineScope()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedBurger by remember { mutableStateOf("Burger du chef") }
    var deliveryTime by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        TextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
        TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
        // Add DropdownMenu for burger selection and TimePickerDialog for deliveryTime here

        Button(onClick = {
            if (firstName.isBlank() || lastName.isBlank() || address.isBlank() || phone.isBlank()) {
                errorMessage = "All fields are required"
            } else {
                val orderDetails = OrderDetails(
                    id_shop = 1,
                    id_user = 353, // This should be dynamically retrieved
                    msg = OrderMessage(
                        firstname = firstName,
                        lastname = lastName,
                        address = address,
                        phone = phone,
                        burger = selectedBurger,
                        delivery_time = deliveryTime
                    )
                )
                coroutineScope.launch {
                    val result = NetworkModule.provideRetrofitService().placeOrder(orderDetails)
                    if (result.isSuccessful) {
                        // Handle success
                    } else {
                        // Handle failure
                    }
                }
            }
        }) {
            Text("Order Now")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}