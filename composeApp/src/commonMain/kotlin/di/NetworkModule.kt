package di

import data.Product
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.dsl.module

fun networkModule()= module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    serializersModule = SerializersModule {
                        polymorphic(Product::class) {
                            subclass(Product::class, Product.serializer())
                        }
                    }

                })
            }
        }
    }

}