import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import di.startKoin
import okio.FileSystem
import org.jetbrains.skiko.wasm.onWasmReady
import root.DefaultRootComponent
import root.RootComponent
import root.RootContent

val koin = startKoin().koin
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("KmpApp2"){
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
            ){
            //    val homeViewModel = HomeViewModel()
              //  val root = DefaultRootComponent(componentContext = DefaultComponentContext(LifecycleRegistry()),homeViewModel)
               val root = koin.get<RootComponent>()
                RootContent(root, modifier = Modifier)
            }
        }
    }
}

fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 100 success image result, without bitmap
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY)
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}
