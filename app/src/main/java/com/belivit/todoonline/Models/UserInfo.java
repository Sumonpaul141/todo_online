package com.belivit.todoonline.Models;

public class UserInfo {
    private String code;

    private String name;

    private String id;

    private String massage;

    private String email;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getMassage ()
    {
        return massage;
    }

    public void setMassage (String massage)
    {
        this.massage = massage;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", name = "+name+", id = "+id+", massage = "+massage+", email = "+email+"]";
    }
}
