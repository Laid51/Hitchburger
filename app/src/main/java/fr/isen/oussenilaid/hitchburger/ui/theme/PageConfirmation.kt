package fr.isen.oussenilaid.hitchburger.ui.theme

import OrderMessage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.oussenilaid.hitchburger.R

@Composable
fun ConfirmationScreen(userOrders: List<OrderMessage>, userName: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.hitchb),
            contentDescription = "Logo DroidBurger",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Merci, $userName! Votre commande a été reçue.",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        OrderList(orders = userOrders)
    }
}

@Composable
fun OrderList(orders: List<OrderMessage>) {
    Column {
        orders.forEach { order ->
            Text(
                text = "Commande pour ${order.firstname} ${order.lastname}: ${order.burger} prévue à ${order.delivery_time}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
