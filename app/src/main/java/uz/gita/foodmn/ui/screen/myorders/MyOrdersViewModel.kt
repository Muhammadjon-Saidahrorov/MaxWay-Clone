package uz.gita.foodmn.ui.screen.myorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.foodmn.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val repastory: AppRepository
) : ViewModel(), MyOrdersContract.ViewModel {

    override val container =
        container<MyOrdersContract.UIState, MyOrdersContract.SideEffect>(MyOrdersContract.UIState())

    override fun onEventDispatcher(intent: MyOrdersContract.Intent) {
        when (intent) {
            MyOrdersContract.Intent.DeleteAll -> {
                repastory.deleteAllFromHistory()
            }
        }
    }

    init {
        repastory.getAllOrderedProductsForHistory().onEach {
            intent { reduce { state.copy(products = it.map { it.toData() }) } }
        }.launchIn(viewModelScope)
    }
}