package uz.gita.foodmn.data.model

import uz.gita.foodmn.data.source.local.entity.OrderedProductEntity

data class OrderedProductData(
    val id:Int,
    val imgUrl: String,
    val price: Long,
    val title: String,
    val count:Int,
    val isBuy:Boolean,
    val day:String
){
    fun toEntity() = OrderedProductEntity(id, imgUrl, price, title, count,isBuy,day)
}