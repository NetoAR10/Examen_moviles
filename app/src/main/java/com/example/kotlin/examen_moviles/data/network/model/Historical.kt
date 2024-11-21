package com.example.kotlin.examen_moviles.data.network.model

data class Historical(
    val date: String,
    val description: String,
    val lang: String,
    val category1: String,
    val category2: String,
    val granularity: String,
    val objectId: String,
)