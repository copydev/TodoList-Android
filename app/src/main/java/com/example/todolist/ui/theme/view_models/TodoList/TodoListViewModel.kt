package com.example.todolist.ui.theme.view_models.TodoList

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.MainApplication
import com.example.todolist.repositories.TodoList.Todo
import com.example.todolist.repositories.TodoList.TodoRepository
import com.example.todolist.ui.theme.view_models.AddTodoList.AddTodoListViewModel
import com.example.todolist.utils.Routes
import com.example.todolist.utils.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


class TodoListViewModel(
    private val repository: TodoRepository
): ViewModel() {
    val todos = repository.getTodos()
    private var lastDeletedTodo: Todo? = null
    private var _uiEvent = Channel<UiEvent>()
    var uiEvent = _uiEvent.receiveAsFlow()

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return TodoListViewModel(
                    (application as MainApplication).todoRepository,
                ) as T
            }
        }
    }


    fun onEvent(event: TodoListEvent) {
        when(event) {
            TodoListEvent.OnAddTodoClick -> {
                sendUiEvents(UiEvent.Navigate(Routes.ADD_EDIT_TODO))

            }
            is TodoListEvent.OnDeleteTodoClick -> {

                viewModelScope.launch {
                    lastDeletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                }
            }
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(event.todo.copy(isDone = event.isDone))
                }
            }
            is TodoListEvent.OnTodoClick -> {
                // Navigate
                sendUiEvents(UiEvent.Navigate(Routes.ADD_EDIT_TODO+"?todoId=${event.todo.id}"))
            }
            TodoListEvent.OnUndoDeleteClick -> {
                viewModelScope.launch {
                    lastDeletedTodo?.let {
                        repository.insertTodo(it)
                    }
                }
            }
        }
    }

    private fun sendUiEvents(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

}