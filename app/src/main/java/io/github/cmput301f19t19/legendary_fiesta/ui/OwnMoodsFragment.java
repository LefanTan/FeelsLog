package io.github.cmput301f19t19.legendary_fiesta.ui;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.MoodEventAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.SpinnerArrayAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.UIEventHandlers.FilterEventHandlers;

public class OwnMoodsFragment extends Fragment {

    private Activity mActivity; //reference to associated activity class, initialized in onAttach function
    private View mView; //reference to associated view, initialized in onCreateView

    private Button deleteMood; //reference to the delete button on own moods page

    private User user;

    private ListView moodList;  //reference to the ListView on own moods page
    private ArrayList<MoodEvent> moodDataList;
    private MoodEventAdapter moodArrayAdapter;
    private int position; //To keep track of which item in the list is selected

    private Spinner filterSpinner;

    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_own_moods, container, false);

        setUpFilterSpinner();

        user = requireActivity().getIntent().getParcelableExtra("USER_PROFILE");

        // When an item in the list is clicked, the delete button appears
        moodDataList = new ArrayList<>();
        loadMoodData();

        moodList = mView.findViewById(R.id.mood_list);
        moodArrayAdapter = new MoodEventAdapter(mActivity, moodDataList);
        moodList.setAdapter(moodArrayAdapter);

        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteMood.setVisibility(View.VISIBLE);
                position = i;
            }
        });


        //When delete button is clicked, the selected item is deleted
        //and the delete button disappears!
        deleteMood = mView.findViewById(R.id.delete_mood);

        deleteMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseHelper.deleteMoodEventById(moodDataList.get(position).getMoodId(), new FirebaseHelper.FirebaseCallback<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Toast.makeText(mActivity, R.string.event_delete_success, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mActivity, R.string.event_delete_fail,
                                Toast.LENGTH_LONG).show();
                    }
                });
                moodDataList.remove(position);
                moodArrayAdapter.notifyDataSetChanged();
                deleteMood.setVisibility(View.INVISIBLE);
            }
        });


        return mView;
    }

    public void loadMoodData() {
        firebaseHelper.getMoodEventsById(user.getUid(), new FirebaseHelper.FirebaseCallback<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                moodDataList.clear();
                for (QueryDocumentSnapshot document : documentSnapshots) {
                    moodDataList.add(document.toObject(MoodEvent.class));
                }
                moodArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // get reference to associated activity
        mActivity = (Activity) context;
    }

    /*
     * Mostly a test function to set up the spinner, populate it with a string array from resource.xml
     */
    private void setUpFilterSpinner() {
        filterSpinner = mView.findViewById(R.id.filter_spinner);
        /*
         * get list of mood from the Mood.moodType enum. Also turn the first letter of each enum to Uppercase
         */
        ArrayList<String> filterArray = new ArrayList<>();
        Mood.MoodTypes.forEach((k, v) -> filterArray.add(k));
        filterArray.add(getResources().getString(R.string.spinner_empty)); //filter_empty is "None"
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
        int defaultIndex = filterArray.indexOf(getResources().getString(R.string.spinner_empty));
        filterSpinner.setSelection(defaultIndex);

        //assign filter selected listener
        filterSpinner.setOnItemSelectedListener(new FilterEventHandlers());
    }

}