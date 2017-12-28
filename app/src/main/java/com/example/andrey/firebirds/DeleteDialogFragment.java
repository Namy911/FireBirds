package com.example.andrey.firebirds;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class DeleteDialogFragment extends DialogFragment {
    public static final String EXTRA_DELETE = "DialogChoice";
    //public static final String ARG_DELETE = "DialogDelete";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.msg_delete_bird)
                .setPositiveButton(R.string.msg_ok_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, 1);
                    }
                })
                .setNegativeButton(R.string.msg_no_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, 0);
                    }
                });

        return builder.create();
    }
    private void sendResult(int resultCode, int action) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DELETE, action);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
//    public static DeleteDialogFragment newInstance() {
//        Bundle args = new Bundle();
//        args.putInt(ARG_DELETE, dialogDelAnswer);
//        DeleteDialogFragment fragment = new DeleteDialogFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
}
