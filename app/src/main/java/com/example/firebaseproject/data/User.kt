package com.example.firebaseproject.data

data class User (
    val firstname : String,
    val lastName: String,
    val email: String,
    val imagePath: String = ""
        ){
    constructor(): this("","","","")
}

