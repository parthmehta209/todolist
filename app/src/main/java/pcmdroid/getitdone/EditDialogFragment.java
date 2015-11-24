package pcmdroid.getitdone;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by parth.mehta on 11/23/15.
 */

public class EditDialogFragment extends DialogFragment {
    private static final String TAG = EditDialogFragment.class.getSimpleName();
    public interface OnCompleteListener {
        void onComplete(String time);
    }
    private OnCompleteListener mListener;
    private EditText editText;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public static final String TODO_ITEM = "item";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_item, container, false);
        getDialog().setTitle(getString(R.string.edit_list_item));
        String todoItemText = getArguments().getString(TODO_ITEM);
        Button saveButton = (Button)v.findViewById(R.id.saveButton);
        editText = (EditText)v.findViewById(R.id.editText);
        editText.setText(todoItemText);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "On Clicking Save on dialog:" + editText.getText().toString());
                mListener.onComplete(editText.getText().toString());
                dismiss();
            }
        });
        return v;
    }
}

