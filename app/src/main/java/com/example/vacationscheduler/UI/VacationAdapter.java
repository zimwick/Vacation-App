package com.example.vacationscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;
        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int position=getAdapterPosition();
                    final Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    intent.putExtra("ID", current.getVacationID());
                    intent.putExtra("Title", current.getVacationTitle());
                    intent.putExtra("Hotel", current.getVacationHotel());
                    intent.putExtra("StartDate", current.getVacationStartDate());
                    intent.putExtra("EndDate", current.getVacationEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(mVacations!=null){
            Vacation current = mVacations.get(position);
            String title = current.getVacationTitle();
            holder.vacationItemView.setText(title);
        }
        else{
            holder.vacationItemView.setText("No vacation title");
        }
    }

    @Override
    public int getItemCount() {
        if(mVacations!=null){
            return mVacations.size();
        }
        else return 0;
    }
    public void setVacations(List<Vacation> vacations){
        mVacations = vacations;
        notifyDataSetChanged();
    }

}
