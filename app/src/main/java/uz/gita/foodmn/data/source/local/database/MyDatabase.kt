package uz.gita.foodmn.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.foodmn.data.source.local.dao.CategoryDao
import uz.gita.foodmn.data.source.local.dao.OrderedProductDao
import uz.gita.foodmn.data.source.local.dao.ProductDao
import uz.gita.foodmn.data.source.local.entity.CategoryEntity
import uz.gita.foodmn.data.source.local.entity.OrderedProductEntity
import uz.gita.foodmn.data.source.local.entity.ProductEntity

@Database(entities = [OrderedProductEntity::class,CategoryEntity::class,ProductEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase:RoomDatabase() {
    abstract fun getDao():OrderedProductDao
    abstract fun getProductDao():ProductDao
    abstract fun getCategoryDao():CategoryDao
}