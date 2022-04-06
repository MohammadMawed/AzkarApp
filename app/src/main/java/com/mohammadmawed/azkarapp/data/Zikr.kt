package com.mohammadmawed.azkarapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zikr")
data class Zikr(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String,
    val repeat: Int,
    val alsabah: Boolean,
    val wasRead: Boolean
) {
}