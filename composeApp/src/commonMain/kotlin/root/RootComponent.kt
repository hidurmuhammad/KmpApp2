package root

import HomeViewModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import data.Product
import detail.DefaultDetailComponent
import detail.DetailComponent
import kotlinx.serialization.Serializable
import list.DefaultListComponent
import list.ListComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed class Child {
      class ListChild(val component: ListComponent):Child()

        class DetailChild(val component: DetailComponent):Child()
    }
}

//Component -> Config(pushing in navigation and passing data) -> deciding the child(childFactory) -> deciding the UI(RootContent)

class DefaultRootComponent(
    private val componentContext: ComponentContext,
    private val homeViewModel: HomeViewModel
):RootComponent,ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> =
       childStack(
           source = navigation,
           serializer = Config.serializer(),
           initialConfiguration = Config.List,
           handleBackButton = true,
           childFactory = ::childFactory
       )

private fun childFactory(config:Config,componentContext: ComponentContext):RootComponent.Child{
   return when (config){
        is Config.List-> RootComponent.Child.ListChild(
       DefaultListComponent(componentContext,homeViewModel) { product ->
navigation.push(Config.Detail(product))
           //it will change the content to Detail
       }
        )

       is Config.Detail-> RootComponent.Child.DetailChild(
           DefaultDetailComponent(componentContext,config.item) {
              onBackClicked()
           }
       )

    }
}
    override fun onBackClicked() {
navigation.pop()
    }

    @Serializable
    sealed interface Config{

        @Serializable
       data object List:Config

        @Serializable
        data class Detail(val item: Product):Config
    }

}