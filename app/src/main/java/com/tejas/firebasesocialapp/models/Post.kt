package com.tejas.firebasesocialapp.models

data class Post(
    val text : String = "",
    val createBy : User = User(),
    val createdAt : Long = 0L,
    val likes: ArrayList<String> = ArrayList()
)
