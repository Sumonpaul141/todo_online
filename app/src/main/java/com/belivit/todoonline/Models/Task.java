package com.belivit.todoonline.Models;

public class Task {
    private String descreiption;

    private String title;

    private String taskId;

    private boolean isArchive;

    public String getDescreiption ()
    {
        return descreiption;
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
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
    public String toString() {
        return "Task{" +
                "descreiption='" + descreiption + '\'' +
                ", title='" + title + '\'' +
                ", taskId='" + taskId + '\'' +
                ", isArchive=" + isArchive +
                '}';
    }
}
