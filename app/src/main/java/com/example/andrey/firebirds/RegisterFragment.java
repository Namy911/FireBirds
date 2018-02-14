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
import com.example.andrey.firebirds.contract.RegisterContract;
import com.example.andrey.firebirds.presenter.RegisterPresenter;
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

public class RegisterFragment extends Fragment implements RegisterContract.RegisterInput,
        RegisterContract.RegisterDisplay{

    private static final String TAG = "EmailPassword";

    @BindView(R.id.reg_login) EditText login;
    @BindView(R.id.reg_pass) EditText pass;
    @BindView(R.id.reg_conf_pass) EditText confPass;
    @BindView(R.id.reg_city) EditText city;
    @BindView(R.id.reg_country) EditText country;
    @BindView(R.id.btn_register) Button btnRegister;
    @BindView(R.id.btn_home) Button btnBack;

//    private DatabaseReference myRef;
//    private UserRepository userRep;

    private RegisterContract.RegisterPresenter presenter;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //userRep  = new UserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();

        presenter = new RegisterPresenter(this, this);

        return view;
    }

    @OnClick({R.id.btn_register, R.id.btn_home})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                presenter.register();
                break;
            case R.id.btn_home:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.account_container, new LoginFragment())
                        .commit();
                break;
        }
    }

    @Override
    public String getLogin() {
        return login.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return pass.getText().toString().trim();
    }

    @Override
    public String getConfirmPass() {
        return confPass.getText().toString().trim();
    }

    @Override
    public String getCity() {
        return city.getText().toString().trim();
    }

    @Override
    public String getCountry() {
        return country.getText().toString().trim();
    }
}
