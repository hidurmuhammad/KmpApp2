import apiClient.httpClient
import data.Product
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.flow

class HomeRepository(
    private val httpClient:HttpClient
) {
    suspend fun getProductApi(): List<Product>{
        val response = httpClient.get("https://fakestoreapi.com/products")
        return  response.body()
    }

    fun getProducts() = flow {
        emit(getProductApi())
    }
}