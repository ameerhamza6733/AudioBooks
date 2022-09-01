package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;

import java.util.Arrays;
import java.util.List;



/**
 * Created by AmeerHamza on 4/25/18.
 */

public class FilterDialogFragment extends DialogFragment {
    private BookSearchDialogFragment.QueryUpdate queryUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_fragment, container, false);
        getDialog().setTitle("Apply filters");
        Spinner spinner = (Spinner) v.findViewById(R.id.SpinnerGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.filters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        List<String> filter = Arrays.asList(getResources().getStringArray(R.array.filters));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (position > 0) {
                    queryUpdate.OnQueryUpdate(Util.INSTANCE.SubjectSortBuilder(MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(),MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, BookFragment.KEY_SHARED_PREF_CURRENT_CATALOG), filter.get(position) + "+" + "asc"));

                    dismiss();
                }
                //  makeApiCall(Util.INSTANCE.quraryBuilder(currentUrl,filter.get(position)+"+"+"asc"));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            queryUpdate = (BookSearchDialogFragment.QueryUpdate) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()
                    + " must implement QueryUpdate");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (queryUpdate != null)
            queryUpdate = null;
    }
}