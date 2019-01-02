package com.example.oscar.hej;

public class GroupUser {

    private String id;
    private String name;
    private String username;



    public GroupUser(String id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;


    }

    public GroupUser(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
