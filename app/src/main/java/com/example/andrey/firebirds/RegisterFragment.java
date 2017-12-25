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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText mLogin, mPass, mConfPass;
    private Button mRegister;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private DatabaseReference mDataBase;
    private static final String TAG = "EmailPassword";

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        mConfPass = view.findViewById(R.id.reg_conf_pass);
        mLogin = view.findViewById(R.id.reg_login);
        mPass = view.findViewById(R.id.reg_pass);
        mRegister = view.findViewById(R.id.btn_register);
        mRegister.setOnClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                register();
                sendEmailVerification();
                break;
            case R.id.btn_home:
                break;
        }
    }

    private void register() {
        String login = mLogin.getText().toString();
        String pass = mPass.getText().toString();
        String confPass = mConfPass.getText().toString();
        if (pass.equals(confPass)) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, BirdsListFragment.newInstance(user.getEmail()))
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
    private void sendEmailVerification() {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());

                        }
                    }
                });
        }

    }
