package com.example.andrey.firebirds;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabAccountFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "Mother";
    private Button button;
    TextView idUser;
    String email;
    String uid;
    private FirebaseAuth mAuth;
    public TabAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();
        email = user.getEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_account, container, false);
        idUser = ((TextView) view.findViewById(R.id.user_id));
        idUser.setText(uid);
        Log.d(TAG, "onCreateView: " + email + uid);
        TextView userEemail = ((TextView) view.findViewById(R.id.user_email));
        userEemail.setText(email);
        button = view.findViewById(R.id.logout);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "onClick: user" + user.getUid());
        } else {
            // No user is signed in
            Intent  intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        
    }
}
