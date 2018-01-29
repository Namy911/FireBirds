package com.example.andrey.firebirds.Repository;

import android.util.Log;

import com.example.andrey.firebirds.model.Pair;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PairRepository extends Repository{

    private static final String TAG = "Mother";
    private final int NO_RECORDS = 0;

    private String idFirstPair;
    private String idSecondPair;
    private String idPair;

    public PairRepository() {
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                    -- Insert Pair --                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Entry point
    public void checkPair(String id, String idSecond){
        idFirstPair = id;
        idSecondPair = idSecond;
        setPairGender();
    }

    // CheckBox parameter (int)gender
    // Set gender
    protected void setPairGender(){
        dataBase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = dataBase.child(TABLE_BIRDS);
        ref.addValueEventListener(new ValueEventListener(){;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // value != unknown
                if (dataSnapshot.child(idFirstPair).child(BIRD_GENDER).getValue() != null) {
                    long gender = (long) dataSnapshot.child(idFirstPair).child(BIRD_GENDER).getValue();

                    switch ((int) gender) {
                        case 1:
                            setPairNodes(BIRD_MALE);
                            Log.d(TAG, "onDataChange: BIRD_MALE");
                            break;
                        case 2:
                            setPairNodes(BIRD_FEMALE);
                            Log.d(TAG, "onDataChange: BIRD_FEMALE");
                            break;
                        // Point - Dialog from warning: delete data //
                        //default:
                    }
                    ref.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Set Node(columns) Pair, set: male from editText, female from dialog
    // Otherwise set: female from editText, male from dialog
    private void setPairNodes(String gender){
        idPair = dataBase.push().getKey();
        Pair pair = new Pair();

        if (gender.equals(BIRD_FEMALE)){
            pair.setFemale(idFirstPair);
            pair.setMale(idSecondPair);
        }else if(gender.equals(BIRD_MALE)){
            pair.setFemale(idSecondPair);
            pair.setMale(idFirstPair);
        }
        dataBase.child(TABLE_PAIRS).child(idPair).setValue(pair);

        checkForeignKeyPair(idFirstPair);
        checkForeignKeyPair(idSecondPair);
    }

    //Check nodes if exist relation
    private void checkForeignKeyPair(final String id){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(TABLE_BIRDS)
                                .child(id).child(FOREIGN_PAIR);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == NO_RECORDS){
                        setForeignKey(id);
                    Log.d(TAG, "onDataChange:  == NO_RECORDS");
                        ref.removeEventListener(this);
                    }else if(dataSnapshot.getChildrenCount() > NO_RECORDS){
                        dataBase.child(TABLE_BIRDS).child(id).child(FOREIGN_PAIR).removeValue();
                        setForeignKey(id);
                    Log.d(TAG, "onDataChange:  > NO_RECORDS");
                        ref.removeEventListener(this);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Set foreign keys in Birds node(Table): Female -> key, Male -> key
    private void setForeignKey(String id){
        HashMap<String, Object> pair = new HashMap<>();
        pair.put(TABLE_BIRDS + "/" + id + "/" + FOREIGN_PAIR + "/" + idPair , "true");
        dataBase.updateChildren(pair);
    }
}
