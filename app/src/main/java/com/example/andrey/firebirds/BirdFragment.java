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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BirdFragment extends Fragment implements View.OnClickListener {
    private static final String DIALOG_DELETE = "DialogDelete";
    private static final String DIALOG_INFO_BIRD = "DialogInfoBird";
    private static final int REQUEST_DELETE = 0;
    private static final int REQUEST_INFO_BIRD = 1;

    public static final String ADD_BIRD = "insert";
    public static final String TABLE_BIRDS = "birds";
    public static final String TABLE_FAMILIES = "families";
    public static final String TABLE_PAIRS = "pairs";

    private String actionBird;
    private Boolean saveData = false;

    private FloatingActionButton btn_action_bird;
    private FloatingActionButton btn_delete_bird;

    private RadioGroup radioGroupGender;
    int IdGender;
    private Bird updateBird;
    private EditText edtName;
    private EditText edtBirdBreed;
    private EditText edtBirdBirth;
    private ImageView imgPair, imgFather, imgMother;
    private TextView txtPair, txtMother, txtFather;
    private TextView txtPairId, txtMotherId, txtFatherId;
    private String idBird;
    private String idCollect;

    private DatabaseReference dataBase;
    private DatabaseReference tableBirds;
    private DatabaseReference tableFamilies;
    private DatabaseReference tablePairs;

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
        tableBirds = dataBase.child(TABLE_BIRDS);
        tableFamilies = dataBase.child(TABLE_FAMILIES);
        tablePairs = dataBase.child(TABLE_PAIRS);

        edtName =  view.findViewById(R.id.edt_bird_name);
        edtBirdBreed =  view.findViewById(R.id.edt_bird_breed);
        edtBirdBirth =  view.findViewById(R.id.edt_birth);
        btn_action_bird = view.findViewById(R.id.floatBtnSave);
        btn_delete_bird = view.findViewById(R.id.floatBtnDelete);
        radioGroupGender = view.findViewById(R.id.rad_gr__gender);
        radioGroupGender = view.findViewById(R.id.rad_gr__gender);

        imgPair = view.findViewById(R.id.img_pair);
        imgFather = view.findViewById(R.id.img_father);
        imgMother = view.findViewById(R.id.img_mother);
        txtPair = view.findViewById(R.id.txt_pair_name);
        txtMother = view.findViewById(R.id.txt_mother_name);
        txtFather = view.findViewById(R.id.txt_father_name);
        txtPairId = view.findViewById(R.id.txt_pair_id);
        txtMotherId = view.findViewById(R.id.txt_mother_id);
        txtFatherId = view.findViewById(R.id.txt_father_id);

        btn_action_bird.setOnClickListener(this);
        btn_delete_bird.setOnClickListener(this);
        imgPair.setOnClickListener(this);
        imgFather.setOnClickListener(this);
        imgMother.setOnClickListener(this);

        if (actionBird.equals(ADD_BIRD)) {
            btn_delete_bird.setVisibility(View.GONE);
            radioGroupGender.check(R.id.radioUnknown);
            getGender();
        }
        getGender();
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
                    //Log.d("Update", actionBird+"");
                        //if (bird.getId().equals(actionBird)){
                        //if (!actionBird.equals(ADD_BIRD)){
                            for (Bird bird : birds) {
                                updateBird = bird;
                                edtBirdBirth.setText(Long.toString(bird.getBirth()));
                                edtName.setText(bird.getName());
                                edtBirdBreed.setText(Long.toString(bird.getBirth()));
                                ((RadioButton)radioGroupGender.getChildAt(bird.getGender())).setChecked(true);
                                //setExtraDate(actionBird);
                                //txtMotherId.setText();
                        }
                    //}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
        }else if (requestCode == REQUEST_INFO_BIRD){
            String value = data.getStringExtra(BirdExtraDialogFragment.EXTRA_INFO_BIRD);
            switch (BirdExtraDialogFragment.member){
                case BirdExtraDialogFragment.PAIR_BIRD :
                    txtPair.setText(value);
                    getIdExtra(BirdExtraDialogFragment.PAIR_BIRD, value);
                    break;
                case BirdExtraDialogFragment.FATHER_BIRD :
                    txtFather.setText(value);
                    getIdExtra(BirdExtraDialogFragment.FATHER_BIRD, value);
                    break;
                case BirdExtraDialogFragment.MOTHER_BIRD :
                    txtMother.setText(value);
                    getIdExtra(BirdExtraDialogFragment.MOTHER_BIRD, value);
                    break;
            }

        }
    }
    private void setExtraDate(String id ){
        Query bird = tableBirds.child(id).child(TABLE_FAMILIES);
        bird.addValueEventListener(new ValueEventListener() {
            List<String> parents = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    parents.add(snapshot.getValue(String.class));
                    Log.d("Parents", parents.size() + "");
                }
                for (String parent : parents) {
                    Log.d("Parents", parent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getIdExtra(final String member, String value){
        Query pair = tableBirds.orderByChild("name").equalTo(value);
        pair.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (member == BirdExtraDialogFragment.PAIR_BIRD){
                        txtPairId.setText(snapshot.getKey());
                    }else if (member == BirdExtraDialogFragment.FATHER_BIRD){
                        txtFatherId.setText(snapshot.getKey());
                    }else if (member == BirdExtraDialogFragment.MOTHER_BIRD){
                        txtMotherId.setText(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getGender(){
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radBtn = group.findViewById(checkedId);
                IdGender = group.indexOfChild(radBtn);
            }
        });
    }
    private void resetInfoBird(View v){
        Snackbar.make(v, R.string.snak_add_bird, Snackbar.LENGTH_LONG)
                .setAction(R.string.snak_action_add_bird, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveData = false;
                        edtName.setText("");
                        edtBirdBreed.setText("");
                        edtBirdBirth.setText("");
                        txtMotherId.setText("Empty");
                        txtMother.setText("Empty");
                        radioGroupGender.check(R.id.radioUnknown);
                        edtName.requestFocus();
                    }
                }).show();
    }
    private void addBirdInfo(View v){
        //String empty = getActivity().getString(R.string.no_data);
        if (v.getId() == R.id.floatBtnSave){
            saveData = true;
        }
        if (actionBird.equals(ADD_BIRD) && saveData == true) {

            idBird = dataBase.push().getKey();
            Bird bird = new Bird(edtName.getText().toString(), edtBirdBreed.getText().toString(), Long.parseLong(edtBirdBirth.getText().toString()), IdGender);
            dataBase.child(TABLE_BIRDS).child(idBird).setValue(bird);

            checkFamily(idBird);

            String idPair = dataBase.push().getKey();
            Pair pair = new Pair(txtPairId.getText().toString());
            dataBase.child(TABLE_PAIRS).child(idPair).setValue(pair);

            dataBase.child(TABLE_PAIRS).child(idPair).child(TABLE_BIRDS).child(idBird).setValue(true);
            dataBase.child(TABLE_BIRDS).child(idBird).child(TABLE_PAIRS).child(idPair).setValue(true);

            resetInfoBird(v);
        }
        saveData = false;
    }

//    private void collectInfoFamily(Map<String,Object> info){
//        ArrayList<String> list = new ArrayList<>();
//
//        for (Map.Entry<String, Object> entry : info.entrySet()){
//            Map singleInfo = (Map) entry.getValue();
//            list.add((String) singleInfo.get("mother"));
//        }
//        Log.d( "Mother", list.toString());
//    }

    private void checkFamily(String idBird){
//        String idFamily = dataBase.push().getKey();
//        Family family = new Family(txtMotherId.getText().toString(), txtFatherId.getText().toString());
//
//        dataBase.child(TABLE_FAMILIES).child(idFamily).setValue(family);
//        dataBase.child(TABLE_FAMILIES).child(idFamily).child(TABLE_BIRDS).child(idBird).setValue(true);
//        dataBase.child(TABLE_BIRDS).child(idBird).child(TABLE_FAMILIES).child(idFamily).setValue(true);

        DatabaseReference ref = dataBase.child(TABLE_FAMILIES);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String id = collectFamily((Map<String, Object>) dataSnapshot.getValue());
                        Log.d("Mother", "collectFamily-------------->" + id);

                        if (collectFamily((Map<String, Object>) dataSnapshot.getValue()) == null) {
                            addFamily();
                            Log.d("Mother", id+"Add New Family--------------------");
                        }else {
                            Log.d("Mother", id+" Update Family Exist++++++++++++++++--------------------");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }
    private void addFamily(){
        String idFamily = dataBase.push().getKey();
        Family family = new Family(txtMotherId.getText().toString(), txtFatherId.getText().toString());

        dataBase.child(TABLE_FAMILIES).child(idFamily).setValue(family);
        dataBase.child(TABLE_FAMILIES).child(idFamily).child(TABLE_BIRDS).child(idBird).setValue(true);
        dataBase.child(TABLE_BIRDS).child(idBird).child(TABLE_FAMILIES).child(idFamily).setValue(true);
    }
    private String collectFamily(Map<String,Object> item) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : item.entrySet()){
            Map singleFamily = (Map) entry.getValue();
            if (singleFamily.get("mother").equals(txtMotherId.getText().toString())){
                Log.d("Mother", "txtMotherId.getText().toString())/////////////////////////////-------------->" + txtMotherId.getText().toString());
                //idCollect = entry.getKey();
                //Log.d("Mother", "entry.getKey-------------->" + idCollect);
                list.add(entry.getKey());
            //}else {
                //idCollect = "";
            }
        }
        idCollect = "";
        if (list.size() > 0) {
            idCollect = "Exist";
        }else {
            idCollect = "";
        }
        Log.d("Mother", "list.size()-------------->" + list.size());
        Log.d("Mother", "list.toString()-------------->" + list.toString());
        return idCollect;
    }

    private void updateBird(){
        if (!actionBird.equals(ADD_BIRD)) {
            Map<String, Object> birdValues = new HashMap<>();
            Bird updateBirdData = new Bird(edtName.getText().toString(),edtBirdBreed.getText().toString(), Long.parseLong(edtBirdBirth.getText().toString()), IdGender);
            birdValues.put(TABLE_BIRDS + "/" + actionBird, updateBirdData);
            dataBase.updateChildren(birdValues);
        }
    }

    @Override
    public void onClick(View v) {
        addBirdInfo(v);
        updateBird();
        switch(v.getId()){
            case R.id.floatBtnDelete:
                DialogFragment deleteDialog = new DeleteDialogFragment();
                deleteDialog.setTargetFragment(BirdFragment.this, REQUEST_DELETE);
                deleteDialog.show(getFragmentManager(), DIALOG_DELETE);
                break;
            case R.id.img_mother:
                DialogFragment motherBirdDialog = BirdExtraDialogFragment.newInstance(BirdExtraDialogFragment.MOTHER_BIRD);
                motherBirdDialog.setTargetFragment(BirdFragment.this, REQUEST_INFO_BIRD);
                motherBirdDialog.show(getFragmentManager(), DIALOG_INFO_BIRD);
                break;
            case R.id.img_father:
                DialogFragment fatherBirdDialog = BirdExtraDialogFragment.newInstance(BirdExtraDialogFragment.FATHER_BIRD);
                fatherBirdDialog.setTargetFragment(BirdFragment.this, REQUEST_INFO_BIRD);
                fatherBirdDialog.show(getFragmentManager(), DIALOG_INFO_BIRD);
                break;
            case R.id.img_pair:
                DialogFragment pairBirdDialog = BirdExtraDialogFragment.newInstance(BirdExtraDialogFragment.PAIR_BIRD);
                pairBirdDialog.setTargetFragment(BirdFragment.this, REQUEST_INFO_BIRD);
                pairBirdDialog.show(getFragmentManager(), DIALOG_INFO_BIRD);
                break;
        }
    }
}
