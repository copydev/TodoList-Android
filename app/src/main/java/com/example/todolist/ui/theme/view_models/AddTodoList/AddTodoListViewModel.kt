package com.example.todolist.ui.theme.view_models.AddTodoList

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todolist.MainApplication
import com.example.todolist.repositories.TodoList.Todo
import com.example.todolist.repositories.TodoList.TodoRepository
import com.example.todolist.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddTodoListViewModel(
    val repository: TodoRepository
): ViewModel() {

    private var _uiEvent = Channel<UiEvent>()
    var uiEvent = _uiEvent.receiveAsFlow()

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf<String>("")
        private set

    var description by mutableStateOf<String>("")
        private set


    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return AddTodoListViewModel(
                    (application as MainApplication).todoRepository,
                ) as T
            }
        }
    }



    fun onEvent(event: AddEditTodoEvent) {
        when(event) {
            is AddEditTodoEvent.init -> {
                Log.v("Check","todo is ${event.todoId}")
                val todoId = event.todoId
                if(todoId != -1) {
                    viewModelScope.launch {
                        repository.getTodoById(todoId)?.let { todo ->
                            title = todo.title
                            description = todo.description ?: ""
                            this@AddTodoListViewModel.todo = todo
                        }
                    }
                }
            }
            is AddEditTodoEvent.OnTitleChange -> {
                title = event.title
            }
            is AddEditTodoEvent.OnDescriptionChange -> {

                description = event.description
            }
            AddEditTodoEvent.OnSaveTodoClick -> {

                viewModelScope.launch {
                    repository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            isDone = todo?.isDone ?: false,
                            id = todo?.id
                        )
                    )
                    sendUiEvents(UiEvent.PopBackStack)
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