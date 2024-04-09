import data.Product
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private  val  _products = MutableStateFlow<List<Product>>(listOf())
    val products = _products.asStateFlow()

    //private val homeRepository = HomeRepository()

    init {
        viewModelScope.launch {
         homeRepository.getProducts().collect{ products ->
             // it  holds the previous value and also products holds the new value
             _products.update { it + products }

         }
        }
    }
}