package com.example.andrey.firebirds;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrey.firebirds.contract.AuthContract;
import com.example.andrey.firebirds.presenter.AuthPresenter;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment implements AuthContract.LoginDisplay,
        AuthContract.LoginInput {

    private static final String TAG = "LoginFragment";

    @BindView(R.id.btn_sing_in) Button btnSingIn;
    @BindView(R.id.google_plus) Button btnGoogle;
    @BindView(R.id.btn_facebook_login) LoginButton btnFacebook;
    @BindView(R.id.edt_login) EditText edtLogin;
    @BindView(R.id.edt_pass) EditText edtPass;
    @BindView(R.id.register) TextView edtRegister;
    @BindView(R.id.forgot_pass) TextView edtForgotPass;

    private CallbackManager callbackManager;

//    private AuthContract.AuthPresenter loginPresenter;
    private AuthContract.LoginPresenter presenter;

//    public void setPresenter(AuthContract.AuthPresenter loginPresenter){
//        this.loginPresenter = loginPresenter;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        presenter = new AuthPresenter(this, this);

        btnFacebook.setFragment(this);
        callbackManager = CallbackManager.Factory.create(); // <-------
        presenter.loginWithFacebook(btnFacebook, callbackManager);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_sing_in, R.id.register,
                R.id.google_plus, R.id.forgot_pass })
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_sing_in :
                presenter.loginWithEmail();
            break;
            case R.id.register :
                openRegisterPage();
                break;
            case R.id.google_plus :
                break;
            case R.id.forgot_pass :
                openForgotPassPage();
                break;
            default: Toast.makeText(getActivity(), " Defaoult Logare", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void openMainPage() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void openRegisterPage() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.account_container, RegisterFragment.newInstance())
                .commit();
    }

    @Override
    public void openForgotPassPage() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.account_container, new ForgotPassFragment())
                .commit();
    }

    @Override
    public void toastErrorLogin() {
        Toast.makeText(getActivity(), "Authentication failed.",
        Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getLogin() {
        return edtLogin.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return edtPass.getText().toString().trim();
    }
}
