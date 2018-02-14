package com.example.andrey.firebirds.contract;

import android.content.Context;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public interface AuthContract {

    interface LoginDisplay {
        void openMainPage();
        void openRegisterPage();
        void openForgotPassPage();
        void toastErrorLogin();
    }

    interface LoginInput{
        String getLogin();
        String getPassword();
    }

    interface LoginPresenter{
        void loginWithEmail();
        void loginWithFacebook(LoginButton loginButton, CallbackManager manager);
    }
}
