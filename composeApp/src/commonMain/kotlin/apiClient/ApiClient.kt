package apiClient

import data.Product
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val httpClient = HttpClient{
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
//            isLenient = true
//            allowStructuredMapKeys = true
//            encodeDefaults = true
//            classDiscriminator = "#class"
            serializersModule = SerializersModule {
                polymorphic(Product::class){
                    subclass(Product::class, Product.serializer())
                }
            }

        })
    }
}