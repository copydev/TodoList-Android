package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.todolist.repositories.TodoList.TodoDatabase
import com.example.todolist.repositories.TodoList.TodoRepositoryImpl
import com.example.todolist.ui.theme.Components.AddTodoList.AddTodoList
import com.example.todolist.ui.theme.view_models.AddTodoList.AddTodoListViewModel
import com.example.todolist.ui.theme.Components.TodoList.TodoListScreen
import com.example.todolist.ui.theme.view_models.TodoList.TodoListViewModel
import com.example.todolist.utils.Routes

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Routes.TODO_LIST
            ) {
                composable(
                    Routes.TODO_LIST,
                ) {
                    TodoListScreen(
                        navigateTo = {
                            navController.navigate(it.route)
                        }
                    )
                }
                composable(
                    Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                    arguments = listOf(navArgument(name = "todoId") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) {
                    AddTodoList(
                        navigateTo = {
                            navController.navigate(it.route)
                        },
                        popBack = { navController.popBackStack() },
                        todoId = it.arguments?.getInt("todoId") ?: -1
                    )
                }
            }
        }
    }
}