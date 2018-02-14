package com.example.andrey.firebirds.contract;


import com.google.firebase.auth.FirebaseUser;

public interface RegisterContract {

    interface RegisterDisplay{
        //void openRegisterPage();
    }


    interface RegisterInput{
        String getLogin();
        String getPassword();
        String getConfirmPass();
        String getCity();
        String getCountry();
    }

    interface RegisterPresenter{
        void register();
    }
}
