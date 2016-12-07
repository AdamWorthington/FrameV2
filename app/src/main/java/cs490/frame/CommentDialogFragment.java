package cs490.frame;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by cwilh on 12/5/2016.
 */
public class CommentDialogFragment extends DialogFragment {
    public interface NoticeDialogListener {
        void onDialogPositiveClick(CommentDialogFragment dialog, String comment);
        void onDialogNegativeClick(CommentDialogFragment dialog);
    }



    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    String existingComment = "";

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public void setExistingComment(String s)
    {
        existingComment = s;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_addcomment, null);
        EditText captionText = (EditText) v.findViewById(R.id.commentEditText);
        if(existingComment != null && !existingComment.isEmpty())
            captionText.setText(existingComment);

        builder.setView(v)
                .setTitle("Add a Note:")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User clicked ok
                        EditText inputtedText = (EditText) v.findViewById(R.id.commentEditText);
                        String note = inputtedText.getText().toString();
                        if(note.isEmpty())
                            mListener.onDialogNegativeClick(CommentDialogFragment.this);
                        else
                            mListener.onDialogPositiveClick (CommentDialogFragment.this, note);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User clicked cancel
                        mListener.onDialogNegativeClick(CommentDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
