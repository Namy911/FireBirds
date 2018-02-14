package com.example.andrey.firebirds.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.andrey.firebirds.contract.RegisterContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPresenter implements RegisterContract.RegisterPresenter{

    private static final String TAG = "RegisterPresenter";

    private RegisterContract.RegisterDisplay registerDisplay;
    private RegisterContract.RegisterInput registerInput;

    private FirebaseAuth mAuth;

    public RegisterPresenter(RegisterContract.RegisterDisplay registerDisplay,
                                RegisterContract.RegisterInput registerInput) {
        this.registerDisplay = registerDisplay;
        this.registerInput = registerInput;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void register() {
        final String login = registerInput.getLogin();
        String pass = registerInput.getPassword();
        String confPass = registerInput.getConfirmPass();
        /*String city = registerInput.getConfirmPass();
        String country = registerInput.getConfirmPass();*/

        if (pass.equals(confPass)) {
//            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser userData = mAuth.getCurrentUser();
                                sendEmailVerification(userData);
                                Log.d(TAG, "onComplete: Success ");
//                                userRep.addUserData(id, login ,city.getText().toString(), country.getText().toString());
//                                context.getSupportFragmentManager().beginTransaction()
//                                        .replace(R.id.account_container, new LoginFragment())
//                                        .commit();
//                                Toast.makeText(context, "Authentication Successful.",
//                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "onComplete: Fail");
//                                Toast.makeText(context, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Log.d(TAG, "register: No confirm");
//            Toast.makeText(context, "No confirm pass.",
//                    Toast.LENGTH_SHORT).show();
        }
    }


    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Verification Success");
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());

                        }
                    }
                });
        }
}
