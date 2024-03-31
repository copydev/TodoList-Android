package com.example.todolist.ui.theme.view_models.AddTodoList

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String): AddEditTodoEvent()
    data class OnDescriptionChange(val description: String): AddEditTodoEvent()
    object OnSaveTodoClick: AddEditTodoEvent()
    data class init(val todoId: Int): AddEditTodoEvent()

}
