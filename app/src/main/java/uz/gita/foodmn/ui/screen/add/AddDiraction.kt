package uz.gita.foodmn.ui.screen.add

import uz.gita.foodmn.util.navigation.AppNavigator
import javax.inject.Inject

interface AddDiraction {
    suspend fun Back()
}

class AddDiractionImpl @Inject constructor(
    private val appNavigator: AppNavigator
): AddDiraction {
    override suspend fun Back() {
        appNavigator.back()
    }

}