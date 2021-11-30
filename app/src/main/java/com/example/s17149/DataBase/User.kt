package com.example.s17149.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User (
    @PrimaryKey(autoGenerate = true)
    var UserId: Int = 0,
    var UserName: String = "No Data"
){

}