package com.infroid.samarpan;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminSearchCitizenFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    AutoCompleteTextView categoryPr, ministry, department, company, position;
    Spinner sector, categoryPu, rank;
    Button btnSearch;
    int activity_id = 3;
    int activity_type = 1;
    /*1 = category private 2 = ministry 3 = department 4 = company 5 = position*/
    ServerLink link = new ServerLink();
    FragmentSwitchListener mCallback;
    WorkExperiences experienceSearch = new WorkExperiences();
    String SEARCH_PRIVATE_CATEGORY = link.SEARCH_PRIVATE_CATEGORY;
    String SEARCH_MINISTRY = link.SEARCH_MINISTRY;
    String SEARCH_DEPARTMENT = link.SEARCH_DEPARTMENT;
    String SEARCH_COMPANY_NAME = link.SEARCH_COMPANY_NAME;
    String SEARCH_POSITION = link.SEARCH_POSITION;
    public AdminSearchCitizenFragment() {
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
        View view = inflater.inflate(R.layout.fragment_admin_search_citizen, container, false);
        sector = (Spinner) view.findViewById(R.id.sector);
        categoryPu = (Spinner) view.findViewById(R.id.categoryPu);
        rank = (Spinner) view.findViewById(R.id.rank);
        categoryPr = (AutoCompleteTextView) view.findViewById(R.id.categoryPr);
        ministry = (AutoCompleteTextView) view.findViewById(R.id.ministry);
        department = (AutoCompleteTextView) view.findViewById(R.id.department);
        company = (AutoCompleteTextView) view.findViewById(R.id.company);
        position = (AutoCompleteTextView) view.findViewById(R.id.position);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        populateSector();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilledDetails();
                int flag = validateFilledDetails();
                if(flag == 0) {
                    AdminResultCitizenFragment.experienceFind = experienceSearch;
                    mCallback.switchFragment(3, "");
                }
            }
        });
        sector.setOnItemSelectedListener(this);
        categoryPu.setOnItemSelectedListener(this);
        rank.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (parent.getId()) {
            case R.id.sector:
                String selectedSector = parent.getItemAtPosition(pos).toString();
                Log.e("sector", selectedSector);
                if(selectedSector.equals("Public Sector")) {
                    Log.e("sector", selectedSector);
                    categoryPu.setVisibility(View.VISIBLE);
                    categoryPr.setVisibility(View.GONE);
                    ministry.setVisibility(View.GONE);
                    department.setVisibility(View.GONE);
                    company.setVisibility(View.GONE);
                    rank.setVisibility(View.GONE);
                    position.setVisibility(View.GONE);
                    experienceSearch.setSector(selectedSector);
                    populateCategoryPublic();
                }
                else if(selectedSector.equals("Private Sector")) {
                    experienceSearch.setSector(selectedSector);
                    categoryPu.setVisibility(View.GONE);
                    ministry.setVisibility(View.GONE);
                    department.setVisibility(View.GONE);
                    categoryPr.setVisibility(View.VISIBLE);
                    company.setVisibility(View.VISIBLE);
                    rank.setVisibility(View.VISIBLE);
                    position.setVisibility(View.VISIBLE);
                    activity_type = 1;
                    AutoCompleteAdapter adapter1 = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
                    activity_type = 4;
                    AutoCompleteAdapter adapter4 = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
                    activity_type = 5;
                    AutoCompleteAdapter adapter5 = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
                    categoryPr.setAdapter(adapter1);
                    company.setAdapter(adapter4);
                    position.setAdapter(adapter5);
                    populateRank();
                }
                break;
            case R.id.categoryPu:
                String selectedInPublic = parent.getItemAtPosition(pos).toString();
                experienceSearch.setCategory(selectedInPublic);
                categoryPr.setVisibility(View.GONE);
                ministry.setVisibility(View.VISIBLE);
                department.setVisibility(View.VISIBLE);
                company.setVisibility(View.VISIBLE);
                rank.setVisibility(View.VISIBLE);
                position.setVisibility(View.VISIBLE);
                activity_type = 2;
                AutoCompleteAdapter adapter2 = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
                activity_type = 3;
                AutoCompleteAdapter adapter3 = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
                activity_type = 4;
                AutoCompleteAdapter adapter4 = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
                activity_type = 5;
                AutoCompleteAdapter adapter5 = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, activity_id, activity_type);
                ministry.setAdapter(adapter2);
                department.setAdapter(adapter3);
                company.setAdapter(adapter4);
                position.setAdapter(adapter5);
                populateRank();
                break;
            case R.id.rank:
                String selectedRank = parent.getItemAtPosition(pos).toString();
                experienceSearch.setRank(selectedRank);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void populateSector() {
        List<String> lables = new ArrayList<>();
        String params[] = {"Sector Type", "Public Sector", "Private Sector"};

        for (int i = 0; i < params.length; i++) {
            lables.add(params[i]);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sector.setAdapter(spinnerAdapter);
    }

    private void populateCategoryPublic() {
        List<String> lables = new ArrayList<>();
        String params[] = {"In Public Sector", "Central Government", "State Government", "Public Sector Units"};

        for (int i = 0; i < params.length; i++) {
            lables.add(params[i]);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categoryPu.setAdapter(spinnerAdapter);
    }

    private void populateRank() {
        List<String> lables = new ArrayList<>();
        String params[] = {"Choose Grade Rank", "Group A", "Group B", "Group C", "Group D"};

        for (int i = 0; i < params.length; i++) {
            lables.add(params[i]);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        rank.setAdapter(spinnerAdapter);
    }

    public void getFilledDetails() {
        if(experienceSearch.getCategory().isEmpty() && experienceSearch.getSector().equals("Private Sector")) {
            experienceSearch.setCategory(categoryPr.getText().toString());
        }
        experienceSearch.setMinistry(ministry.getText().toString());
        experienceSearch.setDepartment(department.getText().toString());
        experienceSearch.setCompany(company.getText().toString());
        experienceSearch.setPosition(position.getText().toString());
    }

    public int validateFilledDetails() {
        int flag = 0;
        if(experienceSearch.getSector().equals("Sector Type") || experienceSearch.getSector().isEmpty()) {
            Toast.makeText(getActivity(), "The sector field is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceSearch.getCategory().equals("In Public Sector") || experienceSearch.getCategory().isEmpty()) {
            Toast.makeText(getActivity(), "The category for the sector is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceSearch.getRank().equals("Choose Grade Rank") || experienceSearch.getRank().isEmpty()) {
            Toast.makeText(getActivity(), "The grade rank field is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        return flag;
    }

    public ArrayList<String> startAsyncPrivateCategory(String... s) {
        ArrayList<String> list = new ArrayList<>();
        try {
            list = new GetPrivateCategory().execute(s).get();
        } catch (Exception e) {
            Log.e("Exc", e.getMessage());
        }
        return list;
    }

    public ArrayList<String> startAsyncMinistry(String... s) {
        ArrayList<String> list = new ArrayList<>();
        try {
            list = new GetMinistry().execute(s).get();
        } catch (Exception e) {
            Log.e("Exc", e.getMessage());
        }
        return list;
    }

    public ArrayList<String> startAsyncDepartment(String... s) {
        ArrayList<String> list = new ArrayList<>();
        try {
            list = new GetDepartment().execute(s).get();
        } catch (Exception e) {
            Log.e("Exc", e.getMessage());
        }
        return list;
    }

    public ArrayList<String> startAsyncCompany(String... s) {
        ArrayList<String> list = new ArrayList<>();
        try {
            list = new GetCompanyName().execute(s).get();
        } catch (Exception e) {
            Log.e("Exc", e.getMessage());
        }
        return list;
    }

    public ArrayList<String> startAsyncPosition(String... s) {
        ArrayList<String> list = new ArrayList<>();
        try {
            list = new GetPosition().execute(s).get();
        } catch (Exception e) {
            Log.e("Exc", e.getMessage());
        }
        return list;
    }

    private class GetPrivateCategory extends AsyncTask<String, Void, ArrayList<String>> {

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
            Log.e("Address = ", SEARCH_PRIVATE_CATEGORY);
            String json = jsonParser.makeServiceCall(SEARCH_PRIVATE_CATEGORY, ServiceHandler.GET, params);
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

    private class GetMinistry extends AsyncTask<String, Void, ArrayList<String>> {

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
            Log.e("Address = ", SEARCH_MINISTRY);
            String json = jsonParser.makeServiceCall(SEARCH_MINISTRY, ServiceHandler.GET, params);
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

    private class GetDepartment extends AsyncTask<String, Void, ArrayList<String>> {

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
            Log.e("Address = ", SEARCH_DEPARTMENT);
            String json = jsonParser.makeServiceCall(SEARCH_DEPARTMENT, ServiceHandler.GET, params);
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

    private class GetCompanyName extends AsyncTask<String, Void, ArrayList<String>> {

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
            Log.e("Address = ", SEARCH_COMPANY_NAME);
            String json = jsonParser.makeServiceCall(SEARCH_COMPANY_NAME, ServiceHandler.GET, params);
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

    private class GetPosition extends AsyncTask<String, Void, ArrayList<String>> {

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
            Log.e("Address = ", SEARCH_POSITION);
            String json = jsonParser.makeServiceCall(SEARCH_POSITION, ServiceHandler.GET, params);
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
}
