import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import root.DefaultRootComponent
import root.RootContent

fun MainViewController() = ComposeUIViewController {

    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        val lifecycle = LifecycleRegistry()
        lifecycle.subscribe(LifecycleCallbackImpl())
        val homeViewModel = HomeViewModel()
        val root = DefaultRootComponent(componentContext = DefaultComponentContext(LifecycleRegistry()),homeViewModel)
      RootContent(root, modifier = Modifier)
    }
}

class LifecycleCallbackImpl: Lifecycle.Callbacks{
    override fun onCreate() {
        super.onCreate()
        println("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
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
              //  directory(getCacheDir().toPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

//private fun getCacheDir(): String {
//    return NSSearchPathForDirectoriesInDomains(
//        NSCachesDirectory,
//        NSUserDomainMask,
//        true,
//    ).first() as String
//}
