package uz.gita.foodmn.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.foodmn.domain.AppRepastory
import uz.gita.foodmn.util.logger
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repastory: AppRepastory
) : ViewModel(), CartContract.ViewModel {

    override val container = container<CartContract.UIState, CartContract.SideEffect>(
        CartContract.UIState()
    )

    override fun onEventDispatcher(intent: CartContract.Intent) {
        when (intent) {
            is CartContract.Intent.DeleteAll -> {
                repastory.deleteAllDataFromBusket(intent.datas)
            }
            is CartContract.Intent.DeleteData -> {
                repastory.deleteFromBusket(intent.data)
            }
            is CartContract.Intent.Update -> {
                repastory.updateProductInBusket(intent.data)
            }
            is CartContract.Intent.Order -> {
                repastory.updateAll(intent.datas)
                intent { postSideEffect(CartContract.SideEffect.Toast("Orders have been shipped !")) }
            }
            is CartContract.Intent.Add -> {
                var bool = false
                var bool2 = true
                repastory.getAllProductForBusket().onEach {

                    it.forEach { data ->
                        if (data.imgUrl == intent.orderedProduct.imgUrl && data.price == intent.orderedProduct.price && data.title == intent.orderedProduct.title) {
                            bool = true
                            return@forEach
                        }
                    }
                    if (bool && bool2) {
//                        intent { postSideEffect(CartContract.SideEffect.Toast("Your order is in the cart !")) }
                    } else if (bool2) {
                        repastory.saveProductForBusket(intent.orderedProduct)
//                        intent { postSideEffect(CartContract.SideEffect.Toast("Your order has been added to the cart !")) }
                        bool2 = false
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    init {
        repastory.getAllProductForBusket().onEach {
            intent { reduce { state.copy(products = it.map { it.toData() }) } }
        }.launchIn(viewModelScope)

        intent { reduce { state.copy(recommendProducts = repastory.getRecommendedProduct()) } }

    }
}