package com.example.s17149.DataBase

import android.app.PendingIntent
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shop(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var description: String,
    var latitude: Double = 0.0,
    var longtitude: Double = 0.0,
    var favorite: Boolean = false,
    var radius: Double = 0.0
) {

}