package uz.gita.foodmn.ui.screen.cart

import org.orbitmvi.orbit.ContainerHost
import uz.gita.foodmn.data.model.OrderedProductData
import uz.gita.foodmn.data.model.ProductData

interface CartContract {
    sealed interface ViewModel:ContainerHost<UIState, SideEffect>{
        fun onEventDispatcher(intent: Intent)
    }
    data class UIState(
        val products:List<OrderedProductData> = emptyList(),
        val recommendProducts: List<ProductData> = emptyList()
    )
    sealed interface SideEffect{
        data class Toast(val m:String): SideEffect
    }
    sealed interface Intent{
        data class DeleteData(val data:OrderedProductData): Intent
        data class DeleteAll(val datas:List<OrderedProductData>): Intent
        data class Update(val data:OrderedProductData): Intent
        data class Order(val datas:List<OrderedProductData>): Intent
        data class Add(val orderedProduct: OrderedProductData): Intent
    }
}