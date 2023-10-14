package uz.gita.foodmn.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.foodmn.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repastory: AppRepository,
    private val diraction: AddDiraction
) : ViewModel(), AddContract.ViewModel {

    override val container =
        container<AddContract.UIState, AddContract.SideEffect>(AddContract.UIState())

    override fun onEventDispatcher(intent: AddContract.Intent) {
        when (intent) {
            is AddContract.Intent.Add -> {
                var bool = false
                var bool2 = true
                repastory.getAllProductForBucket().onEach {
                    it.forEach {
                        if (it.imgUrl == intent.orderedProduct.imgUrl && it.price == intent.orderedProduct.price && it.title == intent.orderedProduct.title) {
                            bool = true
                            return@forEach
                        }
                    }
                    if (bool && bool2) {
                        intent { postSideEffect(AddContract.SideEffect.Toast("Your order is in the cart !")) }
                    } else if(bool2){
                        repastory.saveProductForBucket(intent.orderedProduct)
                        intent { postSideEffect(AddContract.SideEffect.Toast("Your order has been added to the cart !")) }
                        bool2 = false
                    }
                }.launchIn(viewModelScope)
            }
            AddContract.Intent.Back -> {
                viewModelScope.launch {
                    diraction.Back()
                }
            }

        }
    }

    init {

    }
}