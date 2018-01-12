package com.example.andrey.firebirds.Repository;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Repository {

    public static String TAG = "Mother";

    public static final String TABLE_FAMILIES = "families";
    public static final String TABLE_BIRDS = "birds";
    public static final String TABLE_PAIRS = "pairs";

    public static final String FATHER_BIRD = "father";
    public static final String MOTHER_BIRD = "mother";
    public static final String PAIR_BIRD = "pair";
    public static final String BIRD_GENDER = "gender";
    public static final String BIRD_FEMALE = "female";
    public static final String BIRD_MALE = "male";
    public static final String BIRD_UNKNOWN = "unknown";

    public static final String FOREIGN_PAIR = "pair";
    public static final String FOREIGN_BIRD = "bird";
    public static final String FOREIGN_FAMILY = "family";

    protected String idBird;
    protected DatabaseReference dataBase;


//    public void setBirdId(){
//        dataBase = FirebaseDatabase.getInstance().getReference();
//        idBird = dataBase.push().getKey();
//    }
    public String getBirdId(){
        return idBird;
    }


}