package com.belivit.todoonline.Models;

public class Task {
    private String descreiption;

    private String title;

    public String getDescreiption ()
    {
        return descreiption;
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
