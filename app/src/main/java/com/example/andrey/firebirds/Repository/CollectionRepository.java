package com.example.andrey.firebirds.Repository;

import android.util.Log;

import com.example.andrey.firebirds.model.CollectionBirds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionRepository extends Repository {

    private static final String TAG = "CollectionRepository";
    public String setCollectionId(){
         dataBase = FirebaseDatabase.getInstance().getReference();
         String id = dataBase.push().getKey();
         return id;
    }

    public void addCollection( String id,String idUser, String idBird){
        dataBase = FirebaseDatabase.getInstance().getReference();

        //CollectionBirds collection = new CollectionBirds(idUser, id );

        dataBase.child(TABLE_COLLECTIONS).child(id).child(FOREIGN_USER).child(idUser).setValue(true);
        dataBase.child(TABLE_COLLECTIONS).child(id).child(FOREIGN_BIRD).child(idBird).setValue(true);
        checkCollection(idUser);
        setForeignKey(idBird,id);
    }
    private void checkCollection( final  String idUser){
        final DatabaseReference refCollection = dataBase.child(TABLE_COLLECTIONS).child("user");
        refCollection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //collectCollection((Map<String, Object>) dataSnapshot.getValue(), idUser);
                //Map<String, String> list  = new HashMap<>();
                Map<String, String> collectionMap = (HashMap<String, String>) dataSnapshot.getValue();

                collectInfo(collectionMap, idUser);

//                for (Map.Entry<String, CollectionBirds> entry :
//                        collectionMap.entrySet()) {
//                    String user = entry.getKey();
//                    Map users = (Map)entry.getValue();
//                    Log.d(TAG, "onDataChange: " + users.values() + " / " + users.keySet() + " / " +users.get("user"));
//                }
//                refCollection.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void collectInfo(Map<String, String> value, String idUser) {
        ArrayList<String> list = new ArrayList<>();
        Map<String, String> fore = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (value != null) {
            for (Map.Entry<String, String> entry : value.entrySet()) {
                //Log.d(TAG, "collectInfo: Fore " +entry.getValue() );

            }

        }else {
            Log.d(TAG, "collectInfo: Null");
        }
    }

    private void collectCollection(Map<String, java.lang.Object> users, String idUser) {
        ArrayList<String> listUsers = new ArrayList<>();

        for (Map.Entry<String, java.lang.Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            listUsers.add((String) singleUser.get("user"));
        }
        if (listUsers.size() > 0){
            Log.d(TAG, "collectCollection: DA " + listUsers.size());
        }

    }

    private void setForeignKey(String idBird, String idCollection) {
        dataBase.child(TABLE_BIRDS).child(idBird)
                .child(FOREIGN_COLLECTION).child(idCollection).setValue(true);
    }
}
