package com.example.s17149.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shop(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var description: String,
    var latitude: Long = 0L,
    var longtitude: Long = 0L,
    var favorite: Boolean = false,
    var radius: Long = 0L,
) {

}