package com.example.andrey.firebirds;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BirdFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String ADD_BIRD = "insert";
    private String actionBird;

    private Button btn_add_bird;
    private EditText edtName;
    private EditText edtSurname;

    private DatabaseReference dataBase;

    public static BirdFragment newInstance(String action) {
        BirdFragment fragment = new BirdFragment();
        Bundle args = new Bundle();
        args.putString(ADD_BIRD, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actionBird = getArguments().getString(ADD_BIRD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_bird, container, false);
        dataBase = FirebaseDatabase.getInstance().getReference();

        btn_add_bird = view.findViewById(R.id.btn_add_bird);
        edtName =  view.findViewById(R.id.edt_bird_name);
        edtSurname =  view.findViewById(R.id.edt_bird_surname);
        btn_add_bird.setOnClickListener(this);

        if (!actionBird.equals(ADD_BIRD)){
            DatabaseReference refBirds = FirebaseDatabase.getInstance().getReference("birds");

            refBirds.child(actionBird).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Bird> birds = new ArrayList<>();
                    Bird bird1;
                    for (DataSnapshot bird :dataSnapshot.getChildren()) {
                        bird1 = bird.getValue(Bird.class);
                        edtName.setText(bird1.getName());
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (actionBird.equals(ADD_BIRD)) {
            String birdId = dataBase.push().getKey();
            Bird bird = new Bird(birdId, edtName.getText().toString(), edtSurname.getText().toString());
            dataBase.child("birds").child(birdId).setValue(bird);
        }else {
            Log.d("Bundle", "Update");
        }
    }
}
