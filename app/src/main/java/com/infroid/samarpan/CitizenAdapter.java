package com.infroid.samarpan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class CitizenAdapter extends RecyclerView.Adapter<CitizenAdapter.MyViewHolder>{

    private LayoutInflater layoutInflater;
    List<CitizenInfo> data = Collections.emptyList();
    AdminResultCitizenFragment obj = new AdminResultCitizenFragment();
    public CitizenAdapter(Context context, List<CitizenInfo> data) {
        this.data = data;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.citizen_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CitizenInfo current = data.get(position);
        holder.user_id = current.user_id;
        holder.name.setText(current.name);
        holder.address.setText(current.address);
        holder.interests.setText(current.interests);
        holder.dob.setText(current.dob);
        holder.expertise_in.setText(current.expertise_in);
        holder.retirement.setText(current.retirement);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.mCallback.switchFragment(1, current.user_id);
            }
        });

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.mCallback.switchFragment(2, current.user_id);
            }
        });

        holder.btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.mCallback.switchFragment(5, current.user_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, address, dob, expertise_in, retirement, interests;
        String user_id;
        Button btnEdit, btnView, btnResume;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.listName);
            address = (TextView) itemView.findViewById(R.id.listAddress);
            dob = (TextView) itemView.findViewById(R.id.listDOB);
            expertise_in = (TextView) itemView.findViewById(R.id.listExpertise);
            interests = (TextView) itemView.findViewById(R.id.listInterests);
            retirement = (TextView) itemView.findViewById(R.id.listRetirement);
            btnEdit = (Button) itemView.findViewById(R.id.editCitizen);
            btnView = (Button) itemView.findViewById(R.id.viewCitizen);
            btnResume = (Button) itemView.findViewById(R.id.resumeCitizen);
        }
    }
}
