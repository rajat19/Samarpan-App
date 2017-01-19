package com.infroid.samarpan;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExperienceFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    int year, month, day, expAdded = 0;
    Spinner sector, categoryPu, rank;
    EditText categoryPr, ministry, department, company, location, position, role, description;
    Button btnFrom, btnTo, btnAdd;
    TextView from, to;
    Session session;
    DatePickerFragment picker = new DatePickerFragment();
    FragmentSwitchListener mCallback;
    Bundle args;
    WorkExperiences experienceNew = new WorkExperiences();
    ServerLink link = new ServerLink();
    String URL_ADD_EXPERIENCE = link.URL_ADD_EXPERIENCE;
    public AddExperienceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_experience, container, false);
        session = new Session(getContext());
        sector = (Spinner) view.findViewById(R.id.sector);
        categoryPu = (Spinner) view.findViewById(R.id.categoryPu);
        rank = (Spinner) view.findViewById(R.id.rank);
        categoryPr = (EditText) view.findViewById(R.id.categoryPr);
        ministry = (EditText) view.findViewById(R.id.ministry);
        department = (EditText) view.findViewById(R.id.department);
        company = (EditText) view.findViewById(R.id.company);
        location = (EditText) view.findViewById(R.id.location);
        position = (EditText) view.findViewById(R.id.position);
        role = (EditText) view.findViewById(R.id.role);
        description = (EditText) view.findViewById(R.id.description);
        btnFrom = (Button) view.findViewById(R.id.btnFrom);
        btnTo = (Button) view.findViewById(R.id.btnTo);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        from = (TextView) view.findViewById(R.id.from);
        to = (TextView) view.findViewById(R.id.to);
        populateSector();
        setCurrentDateOnView();

        btnFrom.setOnClickListener(this);
        btnTo.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        sector.setOnItemSelectedListener(this);
        categoryPu.setOnItemSelectedListener(this);
        rank.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFrom:
                args = new Bundle();
                args.putInt("year", year);
                args.putInt("month", month);
                args.putInt("day", day);
                Log.e("time", year + month + day+"");
                picker.setArguments(args);
                picker.setCallback(onDateF);
                picker.show(getFragmentManager(), "datePicker");
                break;
            case R.id.btnTo:
                args = new Bundle();
                args.putInt("year", year);
                args.putInt("month", month);
                args.putInt("day", day);
                picker.setArguments(args);
                picker.setCallback(onDateT);
                picker.show(getFragmentManager(), "datePicker");
                break;
            case R.id.btnAdd:
                getFilledDetails();
                int flag = validateFilledDetails();
                if(flag == 0) {
                    new NewWorkExperience().execute();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (parent.getId()) {
            case R.id.sector:
                String selectedSector = parent.getItemAtPosition(pos).toString();
                Log.e("sector", selectedSector);
                if(selectedSector.equals("Public Sector")) {
                    Log.e("sector", selectedSector);
//                    Log.e("se", experienceNew.getSector());
                    categoryPu.setVisibility(View.VISIBLE);
                    categoryPr.setVisibility(View.GONE);
                    ministry.setVisibility(View.GONE);
                    department.setVisibility(View.GONE);
                    company.setVisibility(View.GONE);
                    location.setVisibility(View.GONE);
                    rank.setVisibility(View.GONE);
                    position.setVisibility(View.GONE);
                    role.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
//                    experienceNew.setSector(selectedSector);
                    populateCategoryPublic();
                }
                else if(selectedSector.equals("Private Sector")) {
                    experienceNew.setSector(selectedSector);
                    categoryPu.setVisibility(View.GONE);
                    ministry.setVisibility(View.GONE);
                    department.setVisibility(View.GONE);
                    categoryPr.setVisibility(View.VISIBLE);
                    company.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    rank.setVisibility(View.VISIBLE);
                    position.setVisibility(View.VISIBLE);
                    role.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    populateRank();
                }
                break;
            case R.id.categoryPu:
                String selectedInPublic = parent.getItemAtPosition(pos).toString();
                experienceNew.setCategory(selectedInPublic);
                categoryPr.setVisibility(View.GONE);
                ministry.setVisibility(View.VISIBLE);
                department.setVisibility(View.VISIBLE);
                company.setVisibility(View.VISIBLE);
                location.setVisibility(View.VISIBLE);
                rank.setVisibility(View.VISIBLE);
                position.setVisibility(View.VISIBLE);
                role.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                populateRank();
                break;
            case R.id.rank:
                String selectedRank = parent.getItemAtPosition(pos).toString();
                experienceNew.setRank(selectedRank);
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

    public void setCurrentDateOnView() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        btnTo.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
        btnTo.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
    }

    DatePickerDialog.OnDateSetListener onDateF = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int newyear, int monthOfYear, int dayOfMonth) {
            year = newyear;
            month = monthOfYear;
            day = dayOfMonth;
            btnFrom.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
        }
    };

    DatePickerDialog.OnDateSetListener onDateT = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int newyear, int monthOfYear, int dayOfMonth) {
            year = newyear;
            month = monthOfYear;
            day = dayOfMonth;
            btnTo.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
        }
    };

    public void getFilledDetails() {
        if(experienceNew.getCategory().isEmpty() && experienceNew.getSector().equals("Private Sector")) {
            experienceNew.setCategory(categoryPr.getText().toString());
        }
        experienceNew.setMinistry(ministry.getText().toString());
        experienceNew.setDepartment(department.getText().toString());
        experienceNew.setCompany(company.getText().toString());
        experienceNew.setLocation(location.getText().toString());
        experienceNew.setPosition(position.getText().toString());
        experienceNew.setRole(role.getText().toString());
        experienceNew.setDescription(description.getText().toString());
        experienceNew.setFrom(btnFrom.getText().toString());
        experienceNew.setTo(btnTo.getText().toString());
    }

    public int validateFilledDetails() {
        int flag = 0;
        if(experienceNew.getSector().equals("Sector Type") || experienceNew.getSector().isEmpty()) {
            Toast.makeText(getActivity(), "The sector field is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceNew.getCategory().equals("In Public Sector") || experienceNew.getCategory().isEmpty()) {
            Toast.makeText(getActivity(), "The category for the sector is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceNew.getRank().equals("Choose Grade Rank") || experienceNew.getRank().isEmpty()) {
            Toast.makeText(getActivity(), "The grade rank field is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceNew.getCompany().isEmpty()) {
            Toast.makeText(getActivity(), "The name of company is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceNew.getPosition().isEmpty()) {
            Toast.makeText(getActivity(), "The position held in the company is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceNew.getFrom().isEmpty()) {
            Toast.makeText(getActivity(), "The start date is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(experienceNew.getTo().isEmpty()) {
            Toast.makeText(getActivity(), "The end date is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        return flag;
    }

    private class NewWorkExperience extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", session.getUserId().toString()));
            params.add(new BasicNameValuePair("sector", experienceNew.getSector()));
            params.add(new BasicNameValuePair("category", experienceNew.getCategory()));
            params.add(new BasicNameValuePair("ministry", experienceNew.getMinistry()));
            params.add(new BasicNameValuePair("department", experienceNew.getDepartment()));
            params.add(new BasicNameValuePair("company", experienceNew.getCompany()));
            params.add(new BasicNameValuePair("location", experienceNew.getLocation()));
            params.add(new BasicNameValuePair("from", experienceNew.getFrom()));
            params.add(new BasicNameValuePair("to", experienceNew.getTo()));
            params.add(new BasicNameValuePair("rank", experienceNew.getRank()));
            params.add(new BasicNameValuePair("position", experienceNew.getPosition()));
            params.add(new BasicNameValuePair("role", experienceNew.getRole()));
            params.add(new BasicNameValuePair("description", experienceNew.getDescription()));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_ADD_EXPERIENCE);
            String json = jsonParser.makeServiceCall(URL_ADD_EXPERIENCE, ServiceHandler.GET, params);
            Log.e("Response", "= " + json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray errors = jsonObj.getJSONArray("errors");
                        JSONArray work_experiences = jsonObj.getJSONArray("work_experiences");

                        if (errors.length() > 0) {
                            expAdded = 0;
                            for(int i = 0; i< errors.length(); i++) {
                                
                            }
                            Toast.makeText(getActivity(), errors.get(0).toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            expAdded = 1;
                            for (int i = 0; i < work_experiences.length(); i++) {
                                JSONObject userObj = (JSONObject) work_experiences.get(i);
                                Log.e("Details: ", " >>" + userObj);
                            }
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
            if(expAdded == 1) {
                mCallback.switchFragment(3);
            }
        }
    }
}
