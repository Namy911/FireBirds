package com.example.andrey.firebirds;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrey.firebirds.Repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment /*implements View.OnClickListener*/ {
    private static final String TAG = "EmailPassword";

    @BindView(R.id.reg_login) EditText login;
    @BindView(R.id.reg_pass) EditText pass;
    @BindView(R.id.reg_conf_pass) EditText confPass;
    @BindView(R.id.reg_city) EditText city;
    @BindView(R.id.reg_country) EditText country;

    @BindView(R.id.btn_register) Button btnRegister;
    @BindView(R.id.btn_home) Button btnBack;

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private UserRepository userRep;

    //private DatabaseReference dataBase;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRep  = new UserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

//        login = view.findViewById(R.id.reg_login);
//        confPass = view.findViewById(R.id.reg_conf_pass);
//        pass = view.findViewById(R.id.reg_pass);
//        city = view.findViewById(R.id.reg_city);
//        country = view.findViewById(R.id.reg_country);

//        btnRegister = view.findViewById(R.id.btn_register);
//        btnRegister.setOnClickListener(this);

//        btnBack = view.findViewById(R.id.btn_home);
//        btnBack.setOnClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        return view;
    }

    @OnClick({R.id.btn_register, R.id.btn_home})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                register();
                //Log.d(TAG, "onClick: Register");
                //sendEmailVerification();
                break;
            case R.id.btn_home:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.account_container, new LoginFragment())
                        .commit();
                break;
        }
    }

    private void register() {
        final String login = this.login.getText().toString();
        String pass = this.pass.getText().toString();
        String confPass = this.confPass.getText().toString();
        if (pass.equals(confPass)) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser userData = mAuth.getCurrentUser();
                                String id = userData.getUid();

//                                getActivity().getSupportFragmentManager().beginTransaction()
//                                        .replace(R.id.main_container, BirdsListFragment.newInstance(user.getEmail()))
//                                        .commit();
                                userRep.addUserData(id, login ,city.getText().toString(), country.getText().toString());
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.account_container, new LoginFragment())
                                        .commit();
                                Toast.makeText(getActivity(), "Authentication Successful.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(getActivity(), "No confirm pass.",
                    Toast.LENGTH_SHORT).show();
        }
    }
//    private void sendEmailVerification() {
//        mAuth = FirebaseAuth.getInstance();
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getActivity(),
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//
//                        }
//                    }
//                });
//        }

    }
