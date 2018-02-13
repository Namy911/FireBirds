package com.example.andrey.firebirds.Repository;

import com.example.andrey.firebirds.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository extends Repository {


    public void addUserData(String id, String login, String city, String country){
        dataBase = FirebaseDatabase.getInstance().getReference();
        User user = new User(login, city, country);

        dataBase.child(TABLE_USERS).child(id).setValue(user);
        //setForeignKey(id);
    }

//    private void setForeignKey(String id) {
//        dataBase.child(TABLE_COLLECTIONS).child(idCollection)
//                .child(FOREIGN_USER).child(id).setValue(true);
//    }
}
