package com.example.todolist.ui.theme.Components.TodoList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.repositories.TodoList.TodoRepository
import com.example.todolist.ui.theme.view_models.TodoList.TodoListEvent
import com.example.todolist.ui.theme.view_models.TodoList.TodoListViewModel
import com.example.todolist.utils.UiEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
     viewModel: TodoListViewModel = viewModel(factory = TodoListViewModel.Factory),
     navigateTo: (UiEvent.Navigate) -> Unit
) {
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true) {

        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> navigateTo(it)
                UiEvent.PopBackStack -> {}
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = it.action
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {viewModel.onEvent(TodoListEvent.OnAddTodoClick)}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add-todo")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ){
            todos.value.forEach {
                TodoItem(
                    todo = it,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.clickable {
                        viewModel.onEvent(TodoListEvent.OnTodoClick(it))
                    }
                )
            }
        }
    }
}