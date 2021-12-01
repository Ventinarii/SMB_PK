package com.example.s17149.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0,
    var UserId: String = "",
    var Public: Boolean = false,

    var Name: String,
    var Qty: Float = 0f,
    var Price: Float = 0f,
    var Click: Boolean = false
) {

}