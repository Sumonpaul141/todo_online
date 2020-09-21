package com.belivit.todoonline.Models;

public class AllTodo {
    private String code;

    private Todo[] allTodo;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public Todo[] getAllTodo ()
    {
        return allTodo;
    }

    public void setAllTodo (Todo[] allTodo)
    {
        this.allTodo = allTodo;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", allTodo = "+allTodo+"]";
    }
}
