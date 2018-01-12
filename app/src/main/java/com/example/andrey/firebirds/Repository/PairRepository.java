package com.example.andrey.firebirds.Repository;

import android.util.Log;

import com.example.andrey.firebirds.model.Pair;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PairRepository extends Repository{

    private String idPair ;

    public PairRepository() {
    }

    public void checkPair(String id){
        idPair = id;
        getBirdGender();
    }

    protected void getBirdGender(){
        dataBase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = dataBase.child(TABLE_BIRDS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(idPair).child(BIRD_GENDER).getValue() != null){
                long gender = (long) dataSnapshot.child(idPair).child(BIRD_GENDER).getValue();
                 String id = new Repository().getBirdId();
                switch ((int)gender){
                    case 1 : setGender(BIRD_MALE, id);
                        Log.d("Mother", "22222222222222222222222222" + id);
                        ref.removeEventListener(this);

                        break;
                    case 2 : setGender(BIRD_FEMALE, id);
                        ref.removeEventListener(this);
                        break;
                    default: setGender(BIRD_UNKNOWN, id);break;
                }
            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setGender(String gender, String i){
        String id = dataBase.push().getKey();
        Pair pair = new Pair();
        //dataBase.child(TABLE_PAIRS).child(id).child(gender).setValue(idPair);
        if (gender.equals(BIRD_FEMALE)){
            pair.setFemale(idBird);
            pair.setMale(idPair);
            dataBase.child(TABLE_PAIRS).child(id).setValue(pair);
            Log.d(TAG, "QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ      " + gender + i);

        }else if(gender.equals(BIRD_MALE)){
            //dataBase.child(TABLE_PAIRS).child(id).child(BIRD_FEMALE).setValue(idPair);
            //dataBase.child(TABLE_PAIRS).child(id).child(BIRD_FEMALE).setValue(idBird);
            Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!      " + gender);
        }
    }
}
