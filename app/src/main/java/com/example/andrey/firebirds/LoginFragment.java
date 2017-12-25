package com.example.andrey.firebirds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private Button mSingIn;
    private EditText mLogin, mPass;
    private TextView mRegister;

    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        mLogin = view.findViewById(R.id.edt_login);
        mPass = view.findViewById(R.id.edt_pass);

        mRegister = view.findViewById(R.id.register);
        mRegister.setOnClickListener(this);
        mSingIn = view.findViewById(R.id.btn_sing_in);
        mSingIn.setOnClickListener(this);

        loginWithFacebook((LoginButton) view.findViewById(R.id.login_button));

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_sing_in :
                loginWithEmail();
            break;
            case R.id.register :
                register();
                break;
            default: Toast.makeText(getActivity(), " Defaoult Logare", Toast.LENGTH_SHORT).show();
        }

    }
    private void register() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, RegisterFragment.newInstance())
                .commit();
    }
    private void login() {
        String login = mLogin.getText().toString().trim();
        if (login.equals("da") && Integer.parseInt(mPass.getText().toString().trim())== 1){
            redirect(login);
        }else {
            Toast.makeText(getActivity(), " Logare Eror", Toast.LENGTH_SHORT).show();
        }

    }
    private void loginWithEmail(){
//        String login = mLogin.getText().toString();
//        String pass = mPass.getText().toString();

        String login = "t@t.ru";
        String pass = "123456";
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(login, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            redirect(user.getEmail());
                        } else {
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginWithFacebook(LoginButton loginButton){
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                redirect("Facebook" + loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                redirect("FacebookException" + exception.getMessage());
            }
        });
    }
    private void redirect(String login){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, BirdsListFragment.newInstance(login))
                .commit();
    }

}
