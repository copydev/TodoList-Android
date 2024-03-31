package com.example.todolist.repositories.TodoList

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val description: String?,
    var isDone: Boolean = false,
    @PrimaryKey val id: Int?
)
