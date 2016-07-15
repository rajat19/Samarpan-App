package com.infroid.samarpan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.MyViewHolder> {

	private LayoutInflater inflater;
	List<ExperienceInfo> data = Collections.emptyList();
	public ExperienceAdapter(Context context, List<ExperienceInfo> data){
		inflater = LayoutInflater.from(context); 
		this.data = data;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.experience_layout, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		ExperienceInfo current = data.get(position);
		holder.company.setText(current.company);
        holder.sector.setText(current.sector);
        holder.category.setText(current.category);
        holder.ministry.setText(current.ministry);
        holder.department.setText(current.department);
        holder.rank.setText(current.rank);
        holder.position.setText(current.position);
        holder.role.setText(current.role);
        holder.from.setText(current.from);
        holder.to.setText(current.to);
        holder.location.setText(current.location);
        holder.description.setText(current.description);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

        TextView company, location, to, from, sector, category, ministry, department, rank, position, role, description;
		public MyViewHolder(View itemView) {
			super(itemView);
			company = (TextView) itemView.findViewById(R.id.listCompany);
            location = (TextView) itemView.findViewById(R.id.listLocation);
            from = (TextView) itemView.findViewById(R.id.listFrom);
            to = (TextView) itemView.findViewById(R.id.listTo);
            sector = (TextView) itemView.findViewById(R.id.listSector);
            category = (TextView) itemView.findViewById(R.id.listCategory);
            ministry = (TextView) itemView.findViewById(R.id.listMinistry);
            department = (TextView) itemView.findViewById(R.id.listDepartment);
            rank = (TextView) itemView.findViewById(R.id.listRank);
            position = (TextView) itemView.findViewById(R.id.listPosition);
            role = (TextView) itemView.findViewById(R.id.listRole);
            description = (TextView) itemView.findViewById(R.id.listDescription);
		}
	}
}