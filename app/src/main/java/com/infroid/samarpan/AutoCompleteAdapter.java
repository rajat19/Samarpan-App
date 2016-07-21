package com.infroid.samarpan;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable{

    private ArrayList<String> mData;
    private int activity_id;
    private int actvity_type;
    public AutoCompleteAdapter(Context context, int resource, int id, int type) {
        super(context, resource);
        mData = new ArrayList<>();
        activity_id = id;
        actvity_type = type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    //web api
                    if(activity_id == 2) {
                        AdminSearchViewerFragment obj = new AdminSearchViewerFragment();
                        mData = obj.startAsync(new String[]{constraint.toString()});
                    }
                    if(activity_id == 3) {
                        AdminSearchCitizenFragment obj = new AdminSearchCitizenFragment();
                        switch (actvity_type) {
                            case 1:
                                mData = obj.startAsyncPrivateCategory(new String[]{constraint.toString()});
                                break;
                            case 2:
                                mData = obj.startAsyncMinistry(new String[]{constraint.toString()});
                                break;
                            case 3:
                                mData = obj.startAsyncDepartment(new String[]{constraint.toString()});
                                break;
                            case 4:
                                mData = obj.startAsyncCompany(new String[]{constraint.toString()});
                                break;
                            case 5:
                                mData = obj.startAsyncPosition(new String[]{constraint.toString()});
                                break;
                        }
                    }
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else
                    notifyDataSetInvalidated();
            }
        };
        return myFilter;
    }
}
