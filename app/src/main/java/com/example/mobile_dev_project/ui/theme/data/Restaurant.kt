package com.example.mobile_dev_project.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val description: String = "",
    // Comma-separated tags: "vegan, thai, spicy"
    val tags: String = "",

    // Rating 0–5
    val rating: Float = 0f,

    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
