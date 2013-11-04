package ru.forxy.servlet;

import java.io.Serializable;

/**
 * Created by Uladzislau_Prykhodzk on 11/4/13.
 */
public class User implements Serializable {

    String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
