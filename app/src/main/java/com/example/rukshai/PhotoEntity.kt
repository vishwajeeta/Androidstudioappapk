package com.example.rukshai

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val imagePath: String,

    val note: String = "",

    val createdAt: Long = System.currentTimeMillis()
)
