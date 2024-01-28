package com.mobile.negocio.data.income

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val contact: String,
    val value: Double,
    val quantity: Int,
    val status: Boolean,
    val date: String
)