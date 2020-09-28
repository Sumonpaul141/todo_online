package com.belivit.todoonline.Interface;

import android.view.View;

import com.belivit.todoonline.Models.Todo;

public interface TodoCheckEvent {
    void onTodoChecked(boolean isDone, Todo todo, int position);
    void onTodoDelete(String todoId, int position);
    void onTodoUpdate( Todo todo, int position);
}
