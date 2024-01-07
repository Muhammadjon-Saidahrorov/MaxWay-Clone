package uz.gita.foodmn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.foodmn.ui.screen.add.AddDiraction
import uz.gita.foodmn.ui.screen.add.AddDiractionImpl
import uz.gita.foodmn.ui.screen.products.ProductDirection
import uz.gita.foodmn.ui.screen.products.ProductDiractionImpl

@Module
@InstallIn(SingletonComponent::class)
interface DiractionModule {

    @Binds
    fun bindProductDiraction(impl: ProductDiractionImpl): ProductDirection

    @Binds
    fun bindAddDiraction(impl: AddDiractionImpl): AddDiraction
}