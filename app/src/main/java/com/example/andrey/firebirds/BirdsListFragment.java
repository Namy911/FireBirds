package com.example.andrey.firebirds;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BirdsListFragment extends Fragment implements View.OnClickListener{

    public final static String LOGIN = "login";
    String text;
    Button mButton;

    public static BirdsListFragment newInstance(String login) {
        Bundle args = new Bundle();
        args.putSerializable(LOGIN, login);
        BirdsListFragment fragment = new BirdsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = (String)getArguments().getSerializable(LOGIN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birds_lis, container, false);
        mButton = view.findViewById(R.id.btn_sing_out);
        mButton.setText( "Exit \t" + text);
        mButton.setOnClickListener(this);
        return view;
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new LoginFragment())
                .commit();
        Toast.makeText(getActivity(), "Log Out.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sing_out: signOut();break;
        }
    }
}
