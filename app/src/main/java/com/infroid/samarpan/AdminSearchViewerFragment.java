package com.infroid.samarpan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminSearchViewerFragment extends Fragment {

    AutoCompleteTextView company;
    Button btnSearch;
    private RecyclerView recyclerView;
    private ViewerAdapter adapter;
    FragmentSwitchListener mCallback;
    ProgressDialog progressDialog;
    ServerLink link = new ServerLink();
    String SEARCH_COMPANY = link.SEARCH_COMPANY;
    String URL_ADMIN_VIEWERS = link.URL_ADMIN_VIEWERS;
    List<Details> viewerList = new ArrayList<>();

    public AdminSearchViewerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_admin_search_viewer, container, false);
        company = (AutoCompleteTextView) view.findViewById(R.id.company);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewer);
        // 2 for search viewer
        int activity_id = 2;

        /*only required for searching citizen, Check AdminSearchCitizenFragment.java*/
        int activity_type = 1;
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
        company.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = company.getText().toString();
                new ProfileViewer().execute(s);
            }
        });
        return view;
    }

    public ArrayList<String> startAsync(String... s) {
        ArrayList<String> companiesList = new ArrayList<>();
        try {
            companiesList = new GetCompanies().execute(s).get();
        } catch (Exception e) {
            Log.e("Exc", e.getMessage());
        }
        return companiesList;
    }

    public static List<ViewerInfo> getData(List<Details> viewerList) {
        int l = viewerList.size();
        List<ViewerInfo> data = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            ViewerInfo current = new ViewerInfo();
            current.firstname = viewerList.get(i).getFirstname();
            current.user_id = viewerList.get(i).getUser_id();
            current.description = viewerList.get(i).getDescription();
            current.dob = viewerList.get(i).getDate_of_birth();
            current.location = viewerList.get(i).getAddress_permanent();
            current.website = viewerList.get(i).getWebsite();
            current.expertise_in = viewerList.get(i).getExpertise_in();
            data.add(current);
        }
        return data;
    }

    public void populateRecycler() {
        adapter = new ViewerAdapter(getActivity(), getData(viewerList));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewerList.clear();
    }

    private class GetCompanies extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(String... arg) {
            String search = arg[0];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("term", search));
            ArrayList<String> al = new ArrayList<>();
            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", SEARCH_COMPANY);
            String json = jsonParser.makeServiceCall(SEARCH_COMPANY, ServiceHandler.GET, params);
            Log.e("Response", "= " + json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray list = jsonObj.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject userObj = (JSONObject) list.get(i);
                            Log.e("Complete list: ", " >>" + userObj);
                            String value = userObj.getString("value");
                            al.add(value);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return al;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    private class ProfileViewer extends AsyncTask<String, Void, Void> {

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
        protected Void doInBackground(String... arg) {
            String search = arg[0];
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("term", search));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_ADMIN_VIEWERS);
            String json = jsonParser.makeServiceCall(URL_ADMIN_VIEWERS, ServiceHandler.GET, params);
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
                            completeDetail.setFirstname(userObj.getString("name"));
                            completeDetail.setDate_of_birth(userObj.getString("date_of_birth"));
                            completeDetail.setExpertise_in(userObj.getString("expertise_in"));
                            completeDetail.setWebsite(userObj.getString("website"));
                            completeDetail.setAddress_permanent(userObj.getString("add_permanent"));
                            completeDetail.setDescription(userObj.getString("description"));
                            viewerList.add(completeDetail);
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
            recyclerView.setVisibility(View.VISIBLE);
            populateRecycler();
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
