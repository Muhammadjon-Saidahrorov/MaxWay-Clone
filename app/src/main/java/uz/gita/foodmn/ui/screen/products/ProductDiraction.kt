package uz.gita.foodmn.ui.screen.products

import uz.gita.foodmn.util.navigation.AppNavigator
import uz.gita.foodmn.ui.screen.add.AddScreen
import javax.inject.Inject

interface ProductDiraction {
    suspend fun navigateToAddScreen(addScreen: AddScreen)
}

class ProductDiractionImpl @Inject constructor(
    private val appNavigator: AppNavigator
): ProductDiraction {
    override suspend fun navigateToAddScreen(addScreen: AddScreen) {
        appNavigator.navigateTo(addScreen)
    }

}