package com.infroid.samarpan;

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
		holder.icon.setImageResource(current.iconId);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView company;
		ImageView icon;
		public MyViewHolder(View itemView) {
			super(itemView);
			company = (TextView) itemView.findViewById(R.id.listCompany);
			icon = (ImageView) itemView.findViewById(R.id.listIcon);
		}
	}
}