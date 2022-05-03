package com.example.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class StudentBean(
    @PrimaryKey(autoGenerate = true) var id: Int =0,
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT) var name: String,
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.TEXT) var age: String,
    @ColumnInfo(name = "sex", typeAffinity = ColumnInfo.TEXT) var sex: String
)