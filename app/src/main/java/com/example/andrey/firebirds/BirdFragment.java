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

import com.example.andrey.firebirds.Repository.BirdRepository;
import com.example.andrey.firebirds.Repository.CollectionRepository;
import com.example.andrey.firebirds.Repository.FamilyRepository;
import com.example.andrey.firebirds.Repository.PairRepository;
import com.example.andrey.firebirds.Repository.Repository;
import com.example.andrey.firebirds.model.Bird;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BirdFragment extends Fragment implements View.OnClickListener {

    private String TAG = "Mother";

    private static final String DIALOG_DELETE = "DialogDelete";
    private static final String DIALOG_INFO_BIRD = "DialogInfoBird";
    public static final String ADD_BIRD = "insert";
    private static final int REQUEST_DELETE = 0;
    private static final int REQUEST_INFO_BIRD = 1;

    @BindView(R.id.floatBtnSave) FloatingActionButton btn_action_bird;
    @BindView(R.id.floatBtnDelete) FloatingActionButton btn_delete_bird;
    @BindView(R.id.rad_gr__gender) RadioGroup radioGroupGender;
    @BindView(R.id.edt_bird_name) EditText edtName;
    @BindView(R.id.edt_bird_breed) EditText edtBirdBreed;
    @BindView(R.id.edt_birth) EditText edtBirdBirth;
    @BindView(R.id.img_pair) ImageView imgPair;
    @BindView(R.id.img_father) ImageView imgFather;
    @BindView(R.id.img_mother) ImageView imgMother;
    @BindView(R.id.txt_pair_name) TextView txtPair;
    @BindView(R.id.txt_mother_name) TextView txtMother;
    @BindView(R.id.txt_father_name) TextView txtFather;
    @BindView(R.id.txt_pair_id) TextView txtPairId;
    @BindView(R.id.txt_mother_id) TextView txtMotherId;
    @BindView(R.id.txt_father_id) TextView txtFatherId;

    private String actionBird;
    private Boolean saveData = false;

    int IdGender;
    private Bird updateBird;

    private DatabaseReference dataBase;
    private DatabaseReference tableBirds;

    private String emptyString;

    private String userId;
    private FamilyRepository familyRep;
    private PairRepository pairRep;
    private BirdRepository birdRep;
    private CollectionRepository collectionRep;

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
        emptyString = getResources().getString(R.string.no_data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_bird, container, false);
        ButterKnife.bind(this, view);
        dataBase = FirebaseDatabase.getInstance().getReference();
        tableBirds = dataBase.child(Repository.TABLE_BIRDS);
//        tableFamilies = dataBase.child(TABLE_FAMILIES);
//        tablePairs = dataBase.child(TABLE_PAIRS);

        //edtName =  view.findViewById(R.id.edt_bird_name);
//        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    new Repository().setBirdId();
//                    Log.d("Mother", "111111111111111111111111111" +  new Repository().getBirdId());
//                }
//            }
//        });
        //edtBirdBreed =  view.findViewById(R.id.edt_bird_breed);
        //edtBirdBirth =  view.findViewById(R.id.edt_birth);
        //btn_action_bird = view.findViewById(R.id.floatBtnSave);
        //btn_delete_bird = view.findViewById(R.id.floatBtnDelete);
        //radioGroupGender = view.findViewById(R.id.rad_gr__gender);
        //radioGroupGender = view.findViewById(R.id.rad_gr__gender);
        //imgPair = view.findViewById(R.id.img_pair);
        //imgFather = view.findViewById(R.id.img_father);
        //imgMother = view.findViewById(R.id.img_mother);
//        txtPair = view.findViewById(R.id.txt_pair_name);
//        txtMother = view.findViewById(R.id.txt_mother_name);
//        txtFather = view.findViewById(R.id.txt_father_name);
//        txtPairId = view.findViewById(R.id.txt_pair_id);
//        txtMotherId = view.findViewById(R.id.txt_mother_id);
//        txtFatherId = view.findViewById(R.id.txt_father_id);

//        btn_action_bird.setOnClickListener(this);
//        btn_delete_bird.setOnClickListener(this);
//        imgPair.setOnClickListener(this);
//        imgFather.setOnClickListener(this);
//        imgMother.setOnClickListener(this);

        if (actionBird.equals(ADD_BIRD)) {
            btn_delete_bird.setVisibility(View.GONE);
            radioGroupGender.check(R.id.radioUnknown);
            getGender();
        }
        getGender();
        updateBirdData();

        familyRep  = new FamilyRepository();
        pairRep    = new PairRepository();
        birdRep    = new BirdRepository();
        collectionRep    = new CollectionRepository();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            // No user is signed in
        }
    }

    private void updateBirdData(){
        if (!actionBird.equals(ADD_BIRD)){
            DatabaseReference refBirds = FirebaseDatabase.getInstance().getReference(Repository.TABLE_BIRDS);
            refBirds.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Bird> birds = new ArrayList<>();
                    for (DataSnapshot bird :dataSnapshot.getChildren()) {
                        birds.add(bird.getValue(Bird.class));
                    }
                    for (Bird bird : birds) {
                        updateBird = bird;
                        edtBirdBirth.setText(Long.toString(bird.getBirth()));
                        edtName.setText(bird.getName());
                        edtBirdBreed.setText(Long.toString(bird.getBirth()));
                        ((RadioButton) radioGroupGender.getChildAt(bird.getGender())).setChecked(true);
                    }
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
        if (requestCode == REQUEST_DELETE && data.getExtras().getInt(DialogDeleteFragment.EXTRA_DELETE) == 1) {
            dataBase.child(Repository.TABLE_BIRDS).child(actionBird).setValue(null);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new BirdsListFragment())
                    .commit();
        }else if (requestCode == REQUEST_INFO_BIRD){
            String value = data.getStringExtra(DialogBirdExtraFragment.EXTRA_INFO_BIRD);
            switch (DialogBirdExtraFragment.member){
                case Repository.PAIR_BIRD :
                    txtPair.setText(value);
                    getIdExtra(Repository.PAIR_BIRD, value);
                    break;
                case Repository.FATHER_BIRD :
                    txtFather.setText(value);
                    getIdExtra(Repository.FATHER_BIRD, value);
                    break;
                case Repository.MOTHER_BIRD :
                    txtMother.setText(value);
                    getIdExtra(Repository.MOTHER_BIRD, value);
                    break;
            }

        }
    }
    private void setExtraDate(String id ){
        Query bird = tableBirds.child(id).child(Repository.TABLE_FAMILIES);
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
                    //****************************
                    if (member == Repository.PAIR_BIRD){
                        txtPairId.setText(snapshot.getKey());
                    }else if (member == Repository.FATHER_BIRD){
                        txtFatherId.setText(snapshot.getKey());
                    }else if (member == Repository.MOTHER_BIRD){
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
                        txtMotherId.setText(R.string.no_data);
                        txtMother.setText(R.string.no_data);
                        radioGroupGender.check(R.id.radioUnknown);
                        edtName.requestFocus();
                    }
                }).show();
    }
    //Entry point
    private void addBirdInfo(View v){
        //String empty = getActivity().getString(R.string.no_data);
        if (v.getId() == R.id.floatBtnSave){
            saveData = true;
        }
        if (actionBird.equals(ADD_BIRD) && saveData == true) {

            String birdId = birdRep.setBirdId();
            String collectionId = collectionRep.setCollectionId();

            birdRep.setBird(edtName.getText().toString(), edtBirdBreed.getText().toString(),
                    Long.parseLong(edtBirdBirth.getText().toString()), IdGender, birdId);
            collectionRep.addCollection(collectionId, userId ,birdId);
            //Log.d(TAG, "addBirdInfo: " + userId);
            familyRep.checkFamily(txtMotherId.getText().toString(),txtFatherId.getText().toString(), birdId, emptyString);
            pairRep.checkPair(txtPairId.getText().toString(), birdId);

            resetInfoBird(v);
        }
        saveData = false;
    }

    @OnClick({R.id.floatBtnDelete, R.id.floatBtnSave,
                R.id.img_mother, R.id.img_father, R.id.img_pair})
    public void onClick(View v) {
        addBirdInfo(v);
        //familyRep.updateBird();
        switch(v.getId()){
            case R.id.floatBtnDelete:
                DialogFragment deleteDialog = new DialogDeleteFragment();
                deleteDialog.setTargetFragment(BirdFragment.this, REQUEST_DELETE);
                deleteDialog.show(getFragmentManager(), DIALOG_DELETE);
                break;
            case R.id.img_mother:
                DialogFragment motherBirdDialog = DialogBirdExtraFragment.newInstance(Repository.MOTHER_BIRD);
                motherBirdDialog.setTargetFragment(BirdFragment.this, REQUEST_INFO_BIRD);
                motherBirdDialog.show(getFragmentManager(), DIALOG_INFO_BIRD);
                break;
            case R.id.img_father:
                DialogFragment fatherBirdDialog = DialogBirdExtraFragment.newInstance(Repository.FATHER_BIRD);
                fatherBirdDialog.setTargetFragment(BirdFragment.this, REQUEST_INFO_BIRD);
                fatherBirdDialog.show(getFragmentManager(), DIALOG_INFO_BIRD);
                break;
            case R.id.img_pair:
                DialogFragment pairBirdDialog = DialogBirdExtraFragment.newInstance(Repository.PAIR_BIRD);
                pairBirdDialog.setTargetFragment(BirdFragment.this, REQUEST_INFO_BIRD);
                pairBirdDialog.show(getFragmentManager(), DIALOG_INFO_BIRD);
                break;
        }
    }
}
