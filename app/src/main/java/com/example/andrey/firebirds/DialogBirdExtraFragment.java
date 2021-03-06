package com.example.andrey.firebirds;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.andrey.firebirds.Repository.Repository;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogBirdExtraFragment extends DialogFragment {

    public static final String EXTRA_INFO_BIRD = "name";
    public static final String ARG_PARENT_BIRD = "parent";

    @BindView(R.id.edt_dialog_name) EditText edtName;

    public static String member;

    public static DialogBirdExtraFragment newInstance(String member) {
        Bundle args = new Bundle();
        args.putString(ARG_PARENT_BIRD, member);
        DialogBirdExtraFragment fragment = new DialogBirdExtraFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        member = getArguments().getString(ARG_PARENT_BIRD);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bird_extra, null);
        ButterKnife.bind(this, view);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, edtName.getText().toString());
                    }
                });
        switch(member){
            //************************
            case Repository.FATHER_BIRD : dialog.setTitle(R.string.dialog_father_bird);break;
            case Repository.MOTHER_BIRD : dialog.setTitle(R.string.dialog_mather_bird);break;
            case Repository.PAIR_BIRD   : dialog.setTitle(R.string.dialog_pair_bird);break;

        }
        return dialog.create();
    }

    private void sendResult(int resultCode, String value) {
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_INFO_BIRD, value);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode,intent);

    }
}
