package com.example.andrey.firebirds.Repository;

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
        getBirdGender();
    }

    // CheckBox parameter (int)gender
    // Set gender
    protected void getBirdGender(){
        dataBase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = dataBase.child(TABLE_BIRDS);
        ref.addValueEventListener(new ValueEventListener(){;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(idFirstPair).child(BIRD_GENDER).getValue() != null){
                long gender = (long) dataSnapshot.child(idFirstPair).child(BIRD_GENDER).getValue();

                switch ((int)gender){
                    case 1 : setGender(BIRD_MALE);
                        ref.removeEventListener(this);

                        break;
                    case 2 : setGender(BIRD_FEMALE);
                        ref.removeEventListener(this);
                        break;
                    default: setGender(BIRD_UNKNOWN);break;
                }
            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Set Node(Table) Pair, set: male from editText, female from dialog
    // Otherwise set: female from editText, male from dialog
    private void setGender(String gender){
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
                        ref.removeEventListener(this);
                    }else if(dataSnapshot.getChildrenCount() > NO_RECORDS){
                        // Point - Dialog from warning: delete data //
                        dataBase.child(TABLE_BIRDS).child(id).child(FOREIGN_PAIR).removeValue();
                        setForeignKey(id);
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
