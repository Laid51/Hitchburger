package fr.isen.oussenilaid.hitchburger.ui.theme
import OrderDetails
import OrderMessage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun OrderForm(
    userId: Int,
    onOrderPlaced: (Boolean) -> Unit  // Callback pour gérer la navigation selon le résultat
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedBurger by remember { mutableStateOf("Burger du chef") }
    var deliveryTime by remember { mutableStateOf("Select Time") }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") }
        )
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") }
        )
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") }
        )
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )


        Button(onClick = {
            if (firstName.isBlank() || lastName.isBlank() || address.isBlank() || phone.isBlank() || deliveryTime == "Select Time") {
                errorMessage = "Please fill in all fields"
            } else {
                val orderDetails = OrderDetails(
                    id_shop = 1,
                    id_user = userId,
                    msg = OrderMessage(
                        firstname = firstName,
                        lastname = lastName,
                        address = address,
                        phone = phone,
                        burger = selectedBurger,
                        delivery_time = deliveryTime
                    )
                )
//                sendOrder(orderDetails, onOrderPlaced)
            }
        }) {
            Text("Order Now")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }
    }
}
