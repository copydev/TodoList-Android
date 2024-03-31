package com.example.todolist.ui.theme.Components.AddTodoList

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.ui.theme.view_models.AddTodoList.AddEditTodoEvent
import com.example.todolist.ui.theme.view_models.AddTodoList.AddTodoListViewModel
import com.example.todolist.ui.theme.view_models.TodoList.TodoListViewModel
import com.example.todolist.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoList(
    popBack: () -> Unit,
    viewModel: AddTodoListViewModel = viewModel(factory = AddTodoListViewModel.Factory),
    navigateTo: (UiEvent.Navigate) -> Unit,
    todoId: Int,
) {

    Log.v("Screen", "todoId: ${todoId}")
    LaunchedEffect(key1 = true) {

        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> navigateTo(it)
                UiEvent.PopBackStack -> {popBack()}
                is UiEvent.ShowSnackbar -> {}
            }
        }

        viewModel.onEvent(AddEditTodoEvent.init(todoId = todoId))
    }

    LaunchedEffect(key1 = todoId) {
        viewModel.onEvent(AddEditTodoEvent.init(todoId = todoId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Add todo list")
                },
                navigationIcon = {
                    Box (modifier = Modifier.clickable {
                        popBack()
                    }){
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "add-todo"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick)}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add-todo")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            TextField(
                value = viewModel.title,
                onValueChange = {viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))},
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.description,
                onValueChange = {viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))},
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}