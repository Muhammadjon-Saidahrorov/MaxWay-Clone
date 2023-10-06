package uz.gita.foodmn.ui.screen.myorders

import org.orbitmvi.orbit.ContainerHost
import uz.gita.foodmn.data.model.OrderedProductData

class MyOrdersContract {
    sealed interface ViewModel:ContainerHost<UIState, SideEffect>{
        fun onEventDispatcher(intent: Intent)
    }
    data class UIState(
        val products:List<OrderedProductData> = emptyList()
    )
    sealed interface SideEffect{

    }
    sealed interface Intent{
        object DeleteAll: Intent
    }
}