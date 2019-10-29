package com.liker.android.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.doodle.Profile.model.Phone;
//import com.doodle.R;

import com.liker.android.Profile.model.Phone;
import com.liker.android.R;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Phone> arrayList;
    private boolean isOwnProfile;

    public PhoneAdapter(Context context, ArrayList<Phone> arrayList, boolean isOwnProfile) {
        this.context = context;
        this.arrayList = arrayList;
        this.isOwnProfile = isOwnProfile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_phone, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvPhone.setText(arrayList.get(i).getCountryPhoneCode() + arrayList.get(i).getPhoneNumber());

        if (arrayList.get(i).getPermissionType().equals("1")) {
            viewHolder.tvPermissionIcon.setImageResource(R.drawable.ic_only_me_12dp);
        } else if (arrayList.get(i).getPermissionType().equals("2")) {
            viewHolder.tvPermissionIcon.setImageResource(R.drawable.ic_friends_12dp);
        } else {
            viewHolder.tvPermissionIcon.setImageResource(R.drawable.ic_public_black_24dp);
        }

        if (isOwnProfile) {
            viewHolder.tvPermissionIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvPermissionIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPhone;
        ImageView tvPermissionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPhone = itemView.findViewById(R.id.phone);
            tvPermissionIcon = itemView.findViewById(R.id.permission_icon);
        }
    }

}
