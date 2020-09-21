package com.belivit.todoonline.Models;

public class AllTask {
    private String code;

    private Task[] allTask;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public Task[] getAllTask ()
    {
        return allTask;
    }

    public void setAllTask (Task[] allTask)
    {
        this.allTask = allTask;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", allTask = "+allTask+"]";
    }
}
