package com.example.andrey.firebirds;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabAccountFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "Mother";

    @BindView(R.id.logout) Button btnLogout;
    @BindView(R.id.user_id)TextView idUser;
    @BindView(R.id.user_email) TextView userEemail;

    private String email;
    private String uid;
    private FirebaseAuth mAuth;

    public TabAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            uid = user.getUid();
            email = user.getEmail();
        }else {
            uid = "FB";
            email = "FB-E";
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_account, container, false);
        ButterKnife.bind(this, view);

        idUser.setText(uid);
        userEemail.setText(email);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick(R.id.logout)
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            //Log.d(TAG, "onClick: user" + user.getUid());
        } else {
            // No user is signed in
            Intent  intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
