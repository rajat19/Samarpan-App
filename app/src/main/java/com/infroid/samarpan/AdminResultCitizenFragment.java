package com.infroid.samarpan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminResultCitizenFragment extends Fragment {

    public static WorkExperiences experienceFind;
    private CitizenAdapter adapter;
    ServerLink link = new ServerLink();
    FloatingActionButton fab;
    FragmentSwitchListener mCallback;
    ProgressDialog progressDialog;
    public String URL_ADMIN_CITIZENS = link.URL_ADMIN_CITIZENS;
    List<Details> citizenList = new ArrayList<>();
    RecyclerView recyclerView;
    public AdminResultCitizenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (FragmentSwitchListener) activity;
        } catch (ClassCastException e) {
            Log.d("Error is", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_result_citizen, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewer);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        new SeniorCitizen().execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.switchFragment(7, "");
            }
        });
        return view;
    }

    public static List<CitizenInfo> getData(List<Details> citizenList) {
        int l = citizenList.size();
        List<CitizenInfo> data = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            CitizenInfo current = new CitizenInfo();
            String n = citizenList.get(i).getFirstname() + citizenList.get(i).getMiddlename() + citizenList.get(i).getLastname();
            current.name = n;
            current.user_id = citizenList.get(i).getUser_id();
            current.interests = citizenList.get(i).getInterests();
            current.dob = citizenList.get(i).getDate_of_birth();
            current.address = citizenList.get(i).getAddress_permanent();
            current.retirement = citizenList.get(i).getRetirement();
            current.expertise_in = citizenList.get(i).getExpertise_in();
            data.add(current);
        }
        return data;
    }

    public void switchFragments(int pos, String user_id) {
        mCallback.switchFragment(pos, user_id);
    }

    public void populateRecycler() {
        adapter = new CitizenAdapter(getActivity(), getData(citizenList), AdminResultCitizenFragment.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        citizenList.clear();
    }

    private class SeniorCitizen extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Searching data.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        int flag = 0;

        @Override
        protected Void doInBackground(Void... arg) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("sector", experienceFind.getSector()));
            params.add(new BasicNameValuePair("category", experienceFind.getCategory()));
            params.add(new BasicNameValuePair("ministry", experienceFind.getMinistry()));
            params.add(new BasicNameValuePair("department", experienceFind.getDepartment()));
            params.add(new BasicNameValuePair("company", experienceFind.getCompany()));
            params.add(new BasicNameValuePair("position", experienceFind.getPosition()));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_ADMIN_CITIZENS);
            String json = jsonParser.makeServiceCall(URL_ADMIN_CITIZENS, ServiceHandler.GET, params);
            Log.e("Response", "= " + json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray details = jsonObj.getJSONArray("list");
                        flag = 1;
                        for (int i = 0; i < details.length(); i++) {
                            JSONObject userObj = (JSONObject) details.get(i);
                            Log.e("Details: ", " >>" + userObj);
                            Details completeDetail = new Details();
                            completeDetail.setUser_id(userObj.getString("user_id"));
                            completeDetail.setFirstname(userObj.getString("firstname"));
                            completeDetail.setMiddlename(userObj.getString("middlename"));
                            completeDetail.setLastname(userObj.getString("lastname"));
                            completeDetail.setDate_of_birth(userObj.getString("date_of_birth"));
                            completeDetail.setExpertise_in(userObj.getString("expertise_in"));
                            completeDetail.setRetirement(userObj.getString("retirement"));
                            completeDetail.setAddress_permanent(userObj.getString("add_permanent"));
                            completeDetail.setInterests(userObj.getString("interests"));
                            citizenList.add(completeDetail);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            populateRecycler();
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
