package com.example.andrey.firebirds.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.andrey.firebirds.contract.AuthContract;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthPresenter implements AuthContract.LoginPresenter{

    private static final String TAG = "LoginFragment";

    private FirebaseAuth mAuth;

    private AuthContract.LoginDisplay loginDisplay;
    private AuthContract.LoginInput loginInput;

    public AuthPresenter(AuthContract.LoginDisplay loginDisplay, AuthContract.LoginInput loginInput) {
        this.loginDisplay = loginDisplay;
        this.loginInput = loginInput;
    }

    @Override
    public void loginWithEmail() {
        String login = "t@t.ru";
        String pass = "123456";
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signInWithEmailAndPassword(loginInput.getLogin(), loginInput.getPassword())
        mAuth.signInWithEmailAndPassword(login, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginDisplay.openMainPage();
                        } else {
                            loginDisplay.toastErrorLogin();
                        }
                    }
                });
    }

    @Override
    public void loginWithFacebook(LoginButton loginButton, CallbackManager callbackManager) {
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginDisplay.openMainPage();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "FacebookException: " + exception.getMessage());
            }
        });
    }
}
