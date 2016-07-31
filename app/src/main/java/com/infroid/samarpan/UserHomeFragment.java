package com.infroid.samarpan;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserHomeFragment extends Fragment {

    Intent in;
    TextView textView;
    Button profile;
    FragmentSwitchListener mCallback;
    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (FragmentSwitchListener) activity;
        }
        catch (ClassCastException e) {
            Log.d("Error is", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_home, container, false);
        textView = (TextView) view.findViewById(R.id.txtSamarpan);
        profile = (Button) view.findViewById(R.id.buttonV);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/samarn.ttf");
        textView.setTypeface(type);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.switchFragment(1);
            }
        });
        return view;
    }

}
