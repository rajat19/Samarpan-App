package com.infroid.samarpan;


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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkExperiencesFragment extends Fragment {

	private RecyclerView recyclerView;
	private ExperienceAdapter adapter;
    ServerLink link = new ServerLink();
//    WorkExperiences experiences = new WorkExperiences();
    List<WorkExperiences> experiencesList = new ArrayList<>();
    Session session;
    String URL_WORK_EXPERIENCE = link.URL_WORK_EXPERIENCE;
    ProgressDialog progressDialog;
    public WorkExperiencesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        session = new Session(getContext());
        View view = inflater.inflate(R.layout.fragment_work_experiences, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        int user_id = session.getUserId();
        new WorkExperienceView().execute(user_id);
        return view;
    }

     public static List<ExperienceInfo> getData(List<WorkExperiences> experiencesList) {
        int l = experiencesList.size();
     	List<ExperienceInfo> data = new ArrayList<>();
     	for(int i=0; i < l; i++) {
     		ExperienceInfo current = new ExperienceInfo();
     		current.category = experiencesList.get(i).getCategory();
            current.company = experiencesList.get(i).getCompany();
            current.department = experiencesList.get(i).getDepartment();
            current.description = experiencesList.get(i).getDescription();
            current.from = experiencesList.get(i).getFrom();
            current.location = experiencesList.get(i).getLocation();
            current.ministry = experiencesList.get(i).getMinistry();
            current.position = experiencesList.get(i).getPosition();
            current.rank = experiencesList.get(i).getRank();
            current.role = experiencesList.get(i).getRole();
            current.sector = experiencesList.get(i).getSector();
            current.to = experiencesList.get(i).getTo();
     		data.add(current);
     	}
     	return data;
     }

    public void populateRecycler() {
        adapter = new ExperienceAdapter(getActivity(), getData(experiencesList));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private class WorkExperienceView extends AsyncTask<Integer, Void, Void> {

        int flag = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching Details....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Integer... arg) {
            int uid = arg[0];
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", Integer.toString(uid)));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_WORK_EXPERIENCE);
            String json = jsonParser.makeServiceCall(URL_WORK_EXPERIENCE, ServiceHandler.GET, params);
            Log.e("Response", "= "+json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray work_exp = jsonObj.getJSONArray("work_experiences");
                        flag = 1;
                        for (int i=0; i< work_exp.length(); i++) {
                            JSONObject userObj = (JSONObject) work_exp.get(i);
                            Log.e("Experience: ", " >>"+userObj);
                            WorkExperiences exp = new WorkExperiences();
                            exp.setSector(userObj.getString("sector"));
                            exp.setCategory(userObj.getString("category"));
                            exp.setCompany(userObj.getString("company"));
                            exp.setMinistry(userObj.getString("ministry"));
                            exp.setDepartment(userObj.getString("department"));
                            exp.setRank(userObj.getString("rank"));
                            exp.setRole(userObj.getString("role"));
                            exp.setPosition(userObj.getString("position"));
                            exp.setFrom(userObj.getString("from"));
                            exp.setTo(userObj.getString("to"));
                            exp.setLocation(userObj.getString("location"));
                            exp.setDescription(userObj.getString("description"));
                            experiencesList.add(exp);
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            populateRecycler();
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
