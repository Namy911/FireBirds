package com.example.andrey.firebirds.Repository;

import android.util.Log;

import com.example.andrey.firebirds.model.Bird;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BirdRepository extends Repository {


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                      -- Insert Bird --                                     //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Set id from bird
    public String setBirdId(){
        dataBase = FirebaseDatabase.getInstance().getReference();
        String idBird = dataBase.push().getKey();
        return idBird;
    }

    // Entry point
    // Set Node(Table) Bird
    public void setBird(String name, String breed, Long birth, int gender,  String id){
        dataBase = FirebaseDatabase.getInstance().getReference();
        Bird bird = new Bird(name, breed, birth, gender);
        dataBase.child(TABLE_BIRDS).child(id).setValue(bird);
    }
}
