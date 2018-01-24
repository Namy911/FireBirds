package com.example.andrey.firebirds.Repository;

import android.util.Log;

import com.example.andrey.firebirds.R;
import com.example.andrey.firebirds.model.Bird;
import com.example.andrey.firebirds.model.Family;
import com.example.andrey.firebirds.model.Pair;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FamilyRepository extends Repository {

    private Boolean flagIdCollect;
    private String idInfoFamily;
    private String idMother, idFather, idSon;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  -- Insert Family --                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Entry point
    public void checkFamily(String mother, String father, String son){
        idMother = mother;
        idFather = father;
        idSon = son;
        //Log.d(TAG, "checkFamily: " + idSon);
        dataBase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = dataBase.child(TABLE_FAMILIES);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!collectFamily((Map<String, Object>) dataSnapshot.getValue())) {
                            setFamily();
                            ref.removeEventListener(this);
                        }else {
                            getIdUpdateFamily();
                            ref.removeEventListener(this);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    // Check if mother with that id existing and get her key
    private void getIdUpdateFamily(){
        final DatabaseReference refId = dataBase.child(TABLE_FAMILIES);
        refId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child(MOTHER_BIRD).getValue(String.class).equals(idMother)) {
                        idInfoFamily = snapshot.getKey().toString();
                    }
                }
                if (idInfoFamily != null){
                    addFamilyMember(idInfoFamily);
                }
                refId.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Collect info: if mother have pair return  flag -> true, false
    private Boolean collectFamily(Map<String,Object> item) {
        ArrayList<String> list = new ArrayList<>();
        if (item != null) {
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                Map singleFamily = (Map) entry.getValue();
                if (singleFamily.get(MOTHER_BIRD).equals(idMother)
                        && !singleFamily.get(MOTHER_BIRD).equals(R.string.no_data)) {
                    list.add(entry.getKey());
                }
            }
        }

        flagIdCollect = false;

        if (list.size() > 0) {
            flagIdCollect = true;
        }else {
            flagIdCollect = false;
        }
        return flagIdCollect;
    }

    // Set node(Table) Family with id pair
    private void setFamily(){
        String idFamily = dataBase.push().getKey();
        Family family = new Family(idMother, idFather);

        dataBase.child(TABLE_FAMILIES).child(idFamily).setValue(family);
        dataBase.child(TABLE_FAMILIES).child(idFamily).child(TABLE_BIRDS).child(idSon).setValue(true);
        dataBase.child(TABLE_BIRDS).child(idSon).child(TABLE_FAMILIES).child(idFamily).setValue(true);
    }

    // Add child from existing pair - id: member of family
    private void addFamilyMember(String id){
        HashMap<String, Object> member = new HashMap<>();
        member.put(TABLE_FAMILIES + "/" + id + "/" + TABLE_BIRDS + "/" + idSon , "true");
        dataBase.updateChildren(member);

        setForeignKey(id);
    }

    // Set foreign keys in Birds node(Table): family -> idFamily
    private void setForeignKey(String idFamily){
        dataBase.child(TABLE_BIRDS).child(idSon)
                                   .child(TABLE_FAMILIES)
                                   .child(idFamily)
                                   .setValue(true);
    }

    //public void setBird( String name, String breed, Long birth, int gender){
        //dataBase = FirebaseDatabase.getInstance().getReference();
        //////////////////////////////////////////////////////////////
        //idBird = dataBase.push().getKey();
        //Bird bird = new Bird(name, breed, birth, gender);
        //*********************************************************
        //dataBase.child(TABLE_BIRDS).child(idBird).setValue(bird);
        //dataBase.child(TABLE_BIRDS).child(idBird).setValue(bird);

        //dataBase.child(TABLE_BIRDS).child(getBirdId()).setValue(bird);
    //}

//    public void updateBird(){
//        if (!actionBird.equals(ADD_BIRD)) {
//            Map<String, Object> birdValues = new HashMap<>();
//            Bird updateBirdData = new Bird(edtName.getText().toString(),edtBirdBreed.getText().toString(), Long.parseLong(edtBirdBirth.getText().toString()), IdGender);
//            birdValues.put(TABLE_BIRDS + "/" + actionBird, updateBirdData);
//            dataBase.updateChildren(birdValues);
//        }
//    }
}
