package com.belivit.todoonline.Models;

public class Task {
    private String descreiption;

    private String title;

    private String taskId;

    public String getDescreiption ()
    {
        return descreiption;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setDescreiption (String descreiption)
    {
        this.descreiption = descreiption;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [descreiption = "+descreiption+", title = "+title+"]";
    }
}
