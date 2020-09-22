package com.belivit.todoonline.Interface;

public interface TodoCheckEvent {
    void onTodoChecked(boolean isDone, String todoId);
    void onTodoDelete(String todoId);
}
