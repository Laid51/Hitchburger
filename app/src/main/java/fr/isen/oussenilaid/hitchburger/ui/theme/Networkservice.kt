import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import fr.isen.oussenilaid.hitchburger.ui.theme.ConfirmationScreen
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class OrderDetails(
    val id_shop: Int,
    val id_user: Int,
    val msg: OrderMessage
)

data class OrderMessage(
    val firstname: String,
    val lastname: String,
    val address: String,
    val phone: String,
    val burger: String,
    val delivery_time: String
)

object NetworkModule {
    fun provideRetrofitService(): OrderService {
        return Retrofit.Builder()
            .baseUrl("http://test.api.catering.bluecodegames.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderService::class.java)
    }
}
interface OrderService {
    @POST("user/order")
    suspend fun placeOrder(@Body orderDetails: OrderDetails): Response<Void>

    @POST("listorders")
    suspend fun listOrders(@Body userInfo: UserInfo): Response<List<OrderMessage>>
}

@Composable
fun LoadOrdersScreen(userId: Int) {
    val orders = remember { mutableStateOf<List<OrderMessage>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        coroutineScope.launch {
            val response = NetworkModule.provideRetrofitService().listOrders(UserInfo(id_shop = 1, id_user = userId))
            if (response.isSuccessful) {
                orders.value = response.body() ?: emptyList()
            }
        }
    }

    ConfirmationScreen(userOrders = orders.value, userName = "Utilisateur")
}

data class UserInfo(
    val id_shop: Int,
    val id_user: Int
)
