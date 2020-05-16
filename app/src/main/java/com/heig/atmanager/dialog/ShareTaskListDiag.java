package com.heig.atmanager.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.heig.atmanager.R;

/**
 * Share list Dialog
 */
public class ShareTaskListDiag extends DialogFragment {

    private long taskListId;

    public ShareTaskListDiag(long taskListId) {
        this.taskListId = taskListId;
    }

    /**
     * Interface for the callback
     */
    public interface ShareDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, long taskListId, boolean isEditabl);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    ShareDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ShareDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement ShareDialogueListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final boolean[] isEditable = {false};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(R.string.share_taskList)
                // Add a multichoice, if the list is editable for the receiver
                .setMultiChoiceItems(R.array.share_option, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked)
                   isEditable[which] = true;
                else
                    isEditable[which] = false;

            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick(ShareTaskListDiag.this, taskListId, isEditable[0]);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(ShareTaskListDiag.this);
            }
        });

        return alertBuilder.create();
    }
}