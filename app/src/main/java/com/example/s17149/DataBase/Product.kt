package com.example.s17149.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var qty: Float = 0f,
    var price: Float = 0f,
    var click: Boolean = false,
    var UserId: Long = 0,
    var Public: Boolean = false
) {

}