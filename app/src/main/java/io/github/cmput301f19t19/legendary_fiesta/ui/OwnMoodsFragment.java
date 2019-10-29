package io.github.cmput301f19t19.legendary_fiesta.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.SpinnerArrayAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.UIEventHandlers.FilterEventHandlers;

public class OwnMoodsFragment extends Fragment {

    private Activity mActivity; //reference to associated activity class, initialized in onAttach function
    private View mView; //reference to associated view, initialized in onCreateView

    private Spinner filterSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_own_moods, container, false);

        setUpFilterSpinner();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // get reference to associated activity
        mActivity = (Activity) context;
    }

    /**
     * Mostly a test function to set up the spinner, populate it with a string array from resource.xml
     */
    private void setUpFilterSpinner() {
        filterSpinner = mView.findViewById(R.id.filter_spinner);
        /*
         * get list of mood from the Mood.moodType enum. Also turn the first letter of each enum to Uppercase
         */
        ArrayList<String> filterArray = new ArrayList<>();
        Mood.MoodTypes.forEach((k, v) -> filterArray.add(k));
        filterArray.add(getResources().getString(R.string.filter_empty)); //filter_empty is "None"

        /*
         * convert ArrayList to array, so that it can be passed to SpinnerArrayAdapter
         */
        String[] filterObject = new String[filterArray.size()];
        filterObject = filterArray.toArray(filterObject);

        /*
        Create string ArrayAdapter that will be used for filterSpinner
         */
        ArrayAdapter<String> spinnerAdapter = new SpinnerArrayAdapter(mActivity, R.layout.spinner_item, filterObject);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        filterSpinner.setAdapter(spinnerAdapter);

        //set default selection to None
        int defaultIndex = filterArray.indexOf(getResources().getString(R.string.filter_empty));
        filterSpinner.setSelection(defaultIndex);

        //assign filter selected listener
        filterSpinner.setOnItemSelectedListener(new FilterEventHandlers());
    }

}