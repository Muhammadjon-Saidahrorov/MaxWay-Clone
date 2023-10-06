package uz.gita.foodmn.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.foodmn.data.model.ProductData

@Entity("myProducts")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val categoryId: String,
    val imgUrl: String,
    val info: String,
    val price: Long,
    val title: String
){
    fun toData() = ProductData(id,categoryId, imgUrl, info, price, title)
}