package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;

/**
 * Created by AmeerHamza on 2/15/2018.
 */

public class BookSearchDialogFragment extends DialogFragment {
    String mEncodedEmail;
    EditText mEditTextSearchQueary;
    private QueryUpdate queryUpdate;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static BookSearchDialogFragment newInstance() {
        BookSearchDialogFragment addListDialogFragment = new BookSearchDialogFragment();
        Bundle bundle = new Bundle();
        addListDialogFragment.setArguments(bundle);
        return addListDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            this.queryUpdate = (QueryUpdate) context;
        }catch (Exception e){  throw new ClassCastException(context.toString()
                + " must implement QueryUpdate");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (queryUpdate !=null)
            queryUpdate =null;
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try { getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);}catch (Exception e){e.printStackTrace();}

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_search_queary, null);
        mEditTextSearchQueary = (EditText) rootView.findViewById(R.id.search_queary);
        mEditTextSearchQueary.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    searchQueary();
                }
                return true;
            }
        });

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.positive_button_create, (dialog, id) -> searchQueary());

        return builder.create();
    }

    /**
     * search query
     */
    public void searchQueary() {
        String userEnteredQuery = mEditTextSearchQueary.getText().toString();

        /**
         * If EditText input is not empty
         */
        if (!userEnteredQuery.equals("")) {

            if (queryUpdate !=null)
                queryUpdate.OnQueryUpdate(Util.INSTANCE.quraryBuilder(queryUpdate.toString()));
            BookSearchDialogFragment.this.getDialog().dismiss();
        }
    }

    public interface QueryUpdate {
        void OnQueryUpdate(String query);
    }
}