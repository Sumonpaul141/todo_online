package com.belivit.todoonline.Models;

public class Todo {
    private String todoId;

    private String todoTitle;

    private boolean isDone;

    public String getTodoId ()
    {
        return todoId;
    }

    public void setTodoId (String todoId)
    {
        this.todoId = todoId;
    }

    public String getTodoTitle ()
    {
        return todoTitle;
    }

    public void setTodoTitle (String todoTitle)
    {
        this.todoTitle = todoTitle;
    }

    public boolean getIsDone ()
    {
        return isDone;
    }

    public void setIsDone (boolean isDone)
    {
        this.isDone = isDone;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [todoId = "+todoId+", todoTitle = "+todoTitle+", isDone = "+isDone+"]";
    }
}
