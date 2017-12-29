package com.example.andrey.firebirds;

public class Bird {
    private  String id;
    private String name;
    private String breed;
    private long birth;
    private int gender;

    public Bird() {

    }

    public Bird(String id, String name, String breed, long birth, int gender) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.birth = birth;
        this.gender = gender;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
