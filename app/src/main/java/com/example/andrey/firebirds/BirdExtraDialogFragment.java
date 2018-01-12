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

public class BirdExtraDialogFragment extends DialogFragment {
    private EditText edtName;
    public static final String EXTRA_INFO_BIRD = "name";
    public static final String ARG_PARENT_BIRD = "parent";

//    public static final String FATHER_BIRD = "father";
//    public static final String MOTHER_BIRD = "mother";
//    public static final String PAIR_BIRD = "pair";

    public static String member;

    public static BirdExtraDialogFragment newInstance(String member) {
        Bundle args = new Bundle();
        args.putString(ARG_PARENT_BIRD, member);
        BirdExtraDialogFragment fragment = new BirdExtraDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        member = getArguments().getString(ARG_PARENT_BIRD);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bird_extra, null);
        edtName = view.findViewById(R.id.edt_dialog_name);

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
