package com.example.andrey.firebirds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BirdFragment extends Fragment implements View.OnClickListener {
    private static final String DIALOG_DELETE = "DialogDelete";
    private static final int REQUEST_DELETE = 0;

    public static final String ADD_BIRD = "insert";
    public static final String TABLE_BIRDS = "birds";
    private String actionBird;

    private FloatingActionButton btn_action_bird;
    private FloatingActionButton btn_delete_bird;
    private Bird updateBird;
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

        edtName =  view.findViewById(R.id.edt_bird_name);
        edtSurname =  view.findViewById(R.id.edt_bird_surname);
        btn_action_bird = view.findViewById(R.id.floatBtnSave);
        btn_delete_bird = view.findViewById(R.id.floatBtnDelete);
        //btn_action_bird.setText(getString(R.string.btn_add_bird));
        btn_action_bird.setOnClickListener(this);
        btn_delete_bird.setOnClickListener(this);

        if (actionBird.equals(ADD_BIRD)) {
            btn_delete_bird.setVisibility(View.GONE);
        }

        updateBirdData();
        return view;
    }

    private void updateBirdData(){
        if (!actionBird.equals(ADD_BIRD)){
            DatabaseReference refBirds = FirebaseDatabase.getInstance().getReference(TABLE_BIRDS);
            refBirds.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Bird> birds = new ArrayList<>();
                    for (DataSnapshot bird :dataSnapshot.getChildren()) {
                        birds.add(bird.getValue(Bird.class));
                    }
                    for (Bird bird : birds) {
                        if (bird.getId().equals(actionBird)){
                            updateBird = bird;
                            edtName.setText(bird.getName());
                            edtSurname.setText(bird.getSurname());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //btn_action_bird.setText(getString(R.string.btn_update_bird));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DELETE && data.getExtras().getInt(DeleteDialogFragment.EXTRA_DELETE) == 1) {
            dataBase.child(TABLE_BIRDS).child(actionBird).setValue(null);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new BirdsListFragment())
                    .commit();
        }
    }
    @Override
    public void onClick(View v) {
        if (actionBird.equals(ADD_BIRD)) {
            String birdId = dataBase.push().getKey();
            Bird bird = new Bird(birdId, edtName.getText().toString(), edtSurname.getText().toString());
            dataBase.child(TABLE_BIRDS).child(birdId).setValue(bird);
            Snackbar.make(v, R.string.snak_add_bird, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snak_action_add_bird, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edtName.setText("");
                            edtSurname.setText("");
                            edtName.requestFocus();
                        }
                    }).show();

        }else{
            Map<String, Object> birdValues = new HashMap<>();
            Bird updateBirdData = new Bird(actionBird, edtName.getText().toString(),edtSurname.getText().toString());
            birdValues.put(TABLE_BIRDS + "/" + actionBird, updateBirdData);
            dataBase.updateChildren(birdValues);
        }
        if (v.getId() == R.id.floatBtnDelete){
            DialogFragment deleteDialog = new DeleteDialogFragment();
            deleteDialog.setTargetFragment(BirdFragment.this, REQUEST_DELETE);
            deleteDialog.show(getFragmentManager(), DIALOG_DELETE);
        }
    }
}
