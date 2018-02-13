package com.example.andrey.firebirds.model;

public class CollectionBirds {
    private String userId;
    private String BirdId;

    public CollectionBirds() {
    }

    public CollectionBirds(String userId, String birdId) {
        this.userId = userId;
        BirdId = birdId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBirdId() {
        return BirdId;
    }

    public void setBirdId(String birdId) {
        BirdId = birdId;
    }
}
