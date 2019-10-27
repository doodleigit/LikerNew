package com.liker.android.Setting.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

//import com.doodle.R;
//import com.doodle.Setting.model.Email;
//import com.doodle.Setting.service.EmailModificationListener;

import com.liker.android.R;
import com.liker.android.Setting.model.Email;
import com.liker.android.Setting.service.EmailModificationListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Email> arrayList;
    private EmailModificationListener emailModificationListener;

    public EmailAdapter(Context context, ArrayList<Email> arrayList, EmailModificationListener emailModificationListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.emailModificationListener = emailModificationListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_email, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvEmail.setText(arrayList.get(i).getEmail());

        if (arrayList.get(i).getIsVerified().equals("1")) {
            viewHolder.tvResendVerification.setVisibility(View.GONE);
            if (arrayList.get(i).getType().equals("1")) {
                viewHolder.tvEmailType.setText(context.getText(R.string.primary));
                viewHolder.tvRemove.setVisibility(View.GONE);
            } else {
                viewHolder.tvEmailType.setText(context.getText(R.string.normal));
                viewHolder.tvRemove.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.tvResendVerification.setVisibility(View.VISIBLE);
            viewHolder.tvEmailType.setText(context.getText(R.string.unverified));
            viewHolder.tvRemove.setVisibility(View.VISIBLE);
        }

        viewHolder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailModificationListener.onEmailRemove(arrayList.get(i), i);
            }
        });

        viewHolder.tvResendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailModificationListener.onEmailResendVerification(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmail, tvResendVerification, tvRemove, tvEmailType;
        Spinner emailPrivacySpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.email);
            tvResendVerification = itemView.findViewById(R.id.resend_verification);
            tvRemove = itemView.findViewById(R.id.remove);
            tvEmailType = itemView.findViewById(R.id.email_type);
            emailPrivacySpinner = itemView.findViewById(R.id.email_privacy_spinner);
        }
    }

}
