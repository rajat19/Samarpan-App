package com.infroid.samarpan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

public class ViewerAdapter extends RecyclerView.Adapter<ViewerAdapter.MyViewHolder>{

    private LayoutInflater layoutInflater;
    List<ViewerInfo> data = Collections.emptyList();
    AdminSearchViewerFragment obj = new AdminSearchViewerFragment();
    public ViewerAdapter(Context context,List<ViewerInfo> data) {
        this.data = data;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.viewer_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ViewerInfo current = data.get(position);
        holder.user_id = current.user_id;
        holder.firstname.setText(current.firstname);
        holder.location.setText(current.location);
        holder.description.setText(current.description);
        holder.dob.setText(current.dob);
        holder.expertise_in.setText(current.expertise_in);
        holder.website.setText(current.website);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.mCallback.switchFragment(5, current.user_id);
            }
        });

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.mCallback.switchFragment(6, current.user_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView firstname, location, dob, expertise_in, website, description;
        String user_id;
        Button btnEdit, btnView;
        public MyViewHolder(View itemView) {
            super(itemView);
            firstname = (TextView) itemView.findViewById(R.id.listCompany);
            location = (TextView) itemView.findViewById(R.id.listLocation);
            dob = (TextView) itemView.findViewById(R.id.listDOB);
            expertise_in = (TextView) itemView.findViewById(R.id.listProfession);
            website = (TextView) itemView.findViewById(R.id.listWebsite);
            description = (TextView) itemView.findViewById(R.id.listDescription);
            btnEdit = (Button) itemView.findViewById(R.id.editViewer);
            btnView = (Button) itemView.findViewById(R.id.viewViewer);
        }
    }
}
