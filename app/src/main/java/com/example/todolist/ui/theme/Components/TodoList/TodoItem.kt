package com.example.todolist.ui.theme.Components.TodoList

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolist.repositories.TodoList.Todo
import com.example.todolist.ui.theme.view_models.TodoList.TodoListEvent

@Composable
fun TodoItem (
    todo: Todo,
    onEvent: (TodoListEvent) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = todo.title, modifier = Modifier.weight(1f)
            .background(color = Color.Transparent))
        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete-todo", modifier = Modifier.clickable {
            onEvent(TodoListEvent.OnDeleteTodoClick(todo = todo))
        })
        Checkbox(checked = todo.isDone, onCheckedChange = {
            onEvent(TodoListEvent.OnDoneChange(todo = todo, isDone = it))
        })
    }
}