package com.example.andrey.firebirds.Repository;

import android.util.Log;

import com.example.andrey.firebirds.model.Bird;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BirdRepository extends Repository {

    public void setBird( String name, String breed, Long birth, int gender){
        dataBase = FirebaseDatabase.getInstance().getReference();
        idBird = dataBase.push().getKey();

        Bird bird = new Bird(name, breed, birth, gender);
        dataBase.child(TABLE_BIRDS).child(idBird).setValue(bird);
    }
}
