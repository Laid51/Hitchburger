package fr.isen.oussenilaid.hitchburger.ui.theme

import OrderDetails
import OrderMessage
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderScreen()
        }
    }
}

@Composable
fun OrderScreen() {
    val coroutineScope = rememberCoroutineScope()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedBurger by remember { mutableStateOf("Burger du chef") }
    var deliveryTime by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    OrderForm(
        firstName = firstName,
        lastName = lastName,
        address = address,
        phone = phone,
        selectedBurger = selectedBurger,
        deliveryTime = deliveryTime,
        isLoading = isLoading,
        successMessage = successMessage,
        errorMessage = errorMessage,
        onFirstNameChange = { firstName = it },
        onLastNameChange = { lastName = it },
        onAddressChange = { address = it },
        onPhoneChange = { phone = it },
        onBurgerSelected = { selectedBurger = it },
        onDeliveryTimeChange = { deliveryTime = it },
        onSubmit = {
            coroutineScope.launch {
                isLoading = true
                val orderDetails = OrderDetails(
                    id_shop = 1,
                    id_user = 353,
                    msg = OrderMessage(
                        firstname = firstName,
                        lastname = lastName,
                        address = address,
                        phone = phone,
                        burger = selectedBurger,
                        delivery_time = deliveryTime
                    )
                )
                try {
                    val response = NetworkModule.provideRetrofitService().placeOrder(orderDetails)
                    if (response.isSuccessful) {
                        successMessage = "Order placed successfully!"
                        errorMessage = null
                    } else {
                        errorMessage = "Failed to place order. Please try again."
                        successMessage = null
                    }
                } catch (e: Exception) {
                    errorMessage = "An error occurred: ${e.localizedMessage}"
                    successMessage = null
                } finally {
                    isLoading = false
                }
            }
        }
    )
}

@Composable
fun OrderForm(
    firstName: String,
    lastName: String,
    address: String,
    phone: String,
    selectedBurger: String,
    deliveryTime: String,
    isLoading: Boolean,
    successMessage: String?,
    errorMessage: String?,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBurgerSelected: (String) -> Unit,
    onDeliveryTimeChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = firstName, onValueChange = onFirstNameChange, label = { Text("First Name") })
        TextField(value = lastName, onValueChange = onLastNameChange, label = { Text("Last Name") })
        TextField(value = address, onValueChange = onAddressChange, label = { Text("Address") })
        TextField(value = phone, onValueChange = onPhoneChange, label = { Text("Phone") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        Button(onClick = onSubmit, enabled = !isLoading) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Order Now")
            }
        }
        successMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}
