package uz.gita.foodmn.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.foodmn.data.model.OrderedProductData

@Entity("OrderedProducts")
data class OrderedProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val imgUrl: String,
    val price: Long,
    val title: String,
    val count:Int,
    val isBuy:Boolean,
    val day:String
){
    fun toData() = OrderedProductData(id,imgUrl, price, title, count,isBuy,day)
}