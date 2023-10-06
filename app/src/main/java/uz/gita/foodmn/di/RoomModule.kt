package uz.gita.foodmn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.foodmn.data.source.local.database.MyDatabase
import uz.gita.foodmn.data.source.local.dao.CategoryDao
import uz.gita.foodmn.data.source.local.dao.OrderedProductDao
import uz.gita.foodmn.data.source.local.dao.ProductDao
import uz.gita.foodmn.data.source.local.MyPref
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): MyDatabase =
    Room.databaseBuilder(context, MyDatabase::class.java,"foods.db").allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideDao(db: MyDatabase):OrderedProductDao = db.getDao()

    @Provides
    @Singleton
    fun provideProductDao(db: MyDatabase):ProductDao = db.getProductDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: MyDatabase):CategoryDao = db.getCategoryDao()

    @Provides
    @Singleton
    fun provideMyPref(): MyPref = MyPref()

}