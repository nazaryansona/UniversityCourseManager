
package com.example.universitycoursemanager

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val instructor: String,
    val credits: Int,
    val semester: String
)
