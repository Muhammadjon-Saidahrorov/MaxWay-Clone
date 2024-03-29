package uz.gita.foodmn.domain

import kotlinx.coroutines.flow.Flow
import uz.gita.foodmn.data.model.OrderedProductData
import uz.gita.foodmn.data.model.ProductData
import uz.gita.foodmn.data.model.ProductData2
import uz.gita.foodmn.data.source.local.entity.CategoryEntity
import uz.gita.foodmn.data.source.local.entity.OrderedProductEntity

interface AppRepository {
    fun getAllProducts(): Flow<List<ProductData>>
    fun getSearchedProducts(query:String): Flow<List<ProductData>>
    fun getAllCategory():Flow<List<CategoryEntity>>
    fun getAllCategoriesId():Flow<Result<List<String>>>
    fun getProductsByCategory(categoryName_:String): Flow<Result<List<ProductData2>>>
    fun getRecommendedProduct():List<ProductData>

    fun saveProductForBucket(orderedProductData: OrderedProductData)
    fun getAllProductForBucket():Flow<List<OrderedProductEntity>>
    fun getAllOrderedProductsForHistory(): Flow<List<OrderedProductEntity>>
    fun updateProductInBucket(orderedProductData: OrderedProductData)
    fun updateAll(list:List<OrderedProductData>)
    fun deleteFromBucket(orderedProductData: OrderedProductData)
    fun deleteAllDataFromBucket(list:List<OrderedProductData>)
    fun deleteAllFromHistory()

    fun saveLastSelectedCategory(name:String)
    fun getLastSelectedCategory():Flow<String>


}