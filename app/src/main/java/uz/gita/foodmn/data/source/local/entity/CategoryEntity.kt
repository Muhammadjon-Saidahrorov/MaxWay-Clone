package uz.gita.foodmn.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.foodmn.data.model.CategoryData

@Entity("category")
data class CategoryEntity(
    @PrimaryKey
    val id:String,
    val name:String
){
    fun toData():CategoryData = CategoryData(id,name)
}