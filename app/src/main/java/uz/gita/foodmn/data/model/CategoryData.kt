package uz.gita.foodmn.data.model

import uz.gita.foodmn.data.source.local.entity.CategoryEntity

data class CategoryData(
    val id:String,
    val name:String
){
    fun toEntity():CategoryEntity = CategoryEntity(id,name)
}