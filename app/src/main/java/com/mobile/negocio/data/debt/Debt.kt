package com.mobile.negocio.data.debt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debt")
data class Debt(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val value: Double,
    val details: String,
    val date: String
)