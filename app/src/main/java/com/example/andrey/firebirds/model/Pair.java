package com.example.andrey.firebirds.model;

public class Pair {
    private String female;
    private String male;
    private String unknown;

    public Pair() {
    }

    public Pair(String her, String his, String unknown) {
        this.female = her;
        this.male = his;
        this.unknown = unknown;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getUnknown() {
        return unknown;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }
}
