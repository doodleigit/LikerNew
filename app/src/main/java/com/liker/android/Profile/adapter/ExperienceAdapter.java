package com.liker.android.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.doodle.Profile.model.Experience;
//import com.doodle.Profile.service.AboutComponentUpdateListener;
//import com.doodle.R;

import com.liker.android.Profile.model.Experience;
import com.liker.android.Profile.service.AboutComponentUpdateListener;
import com.liker.android.R;

import java.util.ArrayList;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Experience> arrayList;
    private AboutComponentUpdateListener aboutComponentUpdateListener;
    private boolean isOwnProfile;

    public ExperienceAdapter(Context context, ArrayList<Experience> arrayList, AboutComponentUpdateListener aboutComponentUpdateListener, boolean isOwnProfile) {
        this.context = context;
        this.arrayList = arrayList;
        this.aboutComponentUpdateListener = aboutComponentUpdateListener;
        this.isOwnProfile = isOwnProfile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_experience, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String designation, companyName, duration, summary, fromYear, toYear;
        boolean currentlyWorking;

        designation = arrayList.get(i).getDesignationName();
        companyName = arrayList.get(i).getCompanyName();
        currentlyWorking = arrayList.get(i).getCurrentlyWorked();
        summary = arrayList.get(i).getDescription();
        fromYear = "From " + arrayList.get(i).getFromYear();
        toYear = "to " + arrayList.get(i).getToYear();
        if (currentlyWorking) {
            if (!arrayList.get(i).getFromYear().equals("0") && !arrayList.get(i).getFromYear().equals("")) {
                duration = "From " + arrayList.get(i).getFromYear() + " to " + "present";
            } else {
                duration = "... to " + "present";
            }
        } else {
            duration = (arrayList.get(i).getFromYear().equals("") || arrayList.get(i).getFromYear().equals("0") ? "" : fromYear) + " " + (arrayList.get(i).getToYear().equals("") || arrayList.get(i).getToYear().equals("0") ? "" : toYear);
        }

        viewHolder.tvDesignation.setText(designation);
        viewHolder.tvCompanyName.setText(companyName);
        viewHolder.tvDuration.setText(duration);
        viewHolder.tvSummary.setText(summary);

        if (isOwnProfile) {
            viewHolder.ivEdit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivEdit.setVisibility(View.INVISIBLE);
        }

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutComponentUpdateListener.onExperienceUpdate(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDesignation, tvCompanyName, tvDuration, tvSummary;
        ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDesignation = itemView.findViewById(R.id.designation);
            tvCompanyName = itemView.findViewById(R.id.company_name);
            tvDuration = itemView.findViewById(R.id.duration);
            tvSummary = itemView.findViewById(R.id.summary);
            ivEdit = itemView.findViewById(R.id.edit);
        }
    }

}
