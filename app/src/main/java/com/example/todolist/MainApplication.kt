package com.example.todolist

import android.app.Application
import androidx.room.Room
import com.example.todolist.repositories.TodoList.TodoDatabase
import com.example.todolist.repositories.TodoList.TodoRepository
import com.example.todolist.repositories.TodoList.TodoRepositoryImpl

class MainApplication: Application() {

    lateinit var todoRepository: TodoRepository
    private val DATABASE_NAME: String = "USER_DATABASE"


    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            applicationContext, TodoDatabase::class.java, DATABASE_NAME
        ).build()

        // create instance of DAO to access the entities
        val todoDao = db.todoDao()
        todoRepository = TodoRepositoryImpl(todoDao)

    }
}