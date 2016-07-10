package com.infroid.samarpan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkExperiencesFragment extends Fragment {

	private RecyclerView recyclerView;
	private ExperienceAdapter adapter;
    public WorkExperiencesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_experiences, container, false);
        // recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        // adapter = new ExperienceAdapter(getActivity(), getData());
        // recyclerView.setAdapter(adapter);
        // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    // public static List<ExperienceInfo> getData() {
    // 	List<ExperienceInfo> data = new ArrayList<>();
    // 	// int[] icons = (R.drawable.ic_)
    // 	// String company[]
    // 	for(int i=0; i < company.length; i++) {
    // 		ExperienceInfo current = new ExperienceInfo();
    // 		current.iconId = icons[i];
    // 		current.company = companies[i];
    // 		data.add(current);
    // 	}
    // 	return data;
    // }
}
