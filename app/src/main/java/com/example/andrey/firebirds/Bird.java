package com.example.andrey.firebirds;

public class Bird {
    private  String id;
    private String name;
    private String surname;

    public Bird() {

    }

    public Bird(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
//    public Bird(String name, String surname) {
//        this.name = name;
//        this.surname = surname;
//    }

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
