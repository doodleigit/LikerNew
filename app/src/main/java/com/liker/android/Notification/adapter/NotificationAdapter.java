package com.liker.android.Notification.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Home.service.HomeService;
import com.liker.android.Home.view.activity.StarContributorActivity;
import com.liker.android.Notification.model.Data;
import com.liker.android.Notification.model.NotificationItem;
import com.liker.android.Notification.service.NotificationClickListener;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;
//import com.doodle.App;
//import com.doodle.Home.view.activity.Home;
//import com.doodle.Home.view.activity.StarContributorActivity;
//import com.doodle.Notification.model.NotificationItem;
//import com.doodle.Notification.service.NotificationClickListener;
//import com.doodle.Profile.view.ProfileActivity;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.Tool.Operation.postDateCompare;
import static com.liker.android.Tool.Tools.isNullOrEmpty;

//import static com.doodle.Tool.Operation.postDateCompare;
//import static com.doodle.Tool.Tools.isNullOrEmpty;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NotificationItem> arrayList;
    private NotificationClickListener notificationClickListener;
    private String token, deviceId, userId, profileId, userName, profileName, selectedCategory = "";
    private PrefManager manager;
    private HomeService webService;
    private boolean networkOk;
    private ProgressDialog progressDialog;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> arrayList, NotificationClickListener notificationClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.notificationClickListener = notificationClickListener;
        manager = new PrefManager(context);
        progressDialog = new ProgressDialog(context);
        networkOk = NetworkHelper.hasNetworkAccess(context);
        webService = HomeService.mRetrofit.create(HomeService.class);
        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        userName = manager.getUserName();
        profileName = manager.getProfileName();
        profileId = manager.getProfileId();
        token = manager.getToken();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String details, photo, type, seen;
        long time;
        NotificationItem notificationItem = arrayList.get(i);
        Log.d("NotificationItem: ", notificationItem.toString());
        details = arrayList.get(i).getText();
        type = arrayList.get(i).getData().getNotifType();
        seen = arrayList.get(i).getData().getHasSeenDetails();
        photo = AppConstants.PROFILE_IMAGE + arrayList.get(i).getData().getPhoto();
        time = Long.valueOf(arrayList.get(i).getData().getTimeSent());

        viewHolder.notificationDetails.setText(Tools.colorBackground(details));
        //  viewHolder.notificationTime.setText(getDate(time));
        viewHolder.notificationTime.setText(postDateCompare(context, time * 1000));

        if (seen.equals("0")) {
            viewHolder.mainLayout.setBackgroundResource(R.color.colorMainBackground);
        } else {
            viewHolder.mainLayout.setBackgroundResource(R.color.colorWhite);
        }

        if (!isNullOrEmpty(photo)) {
            Glide.with(App.getAppContext())
                    .load(photo)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.imageNotification);
        }

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClickListener.onNotificationClick(arrayList.get(i).getData().getId());
                executeAction(Tools.getNotificationTypeActionType(type), arrayList.get(i));
                arrayList.get(i).getData().setHasSeenDetails("1");
                notifyItemChanged(i);
            }
        });

        Data data = notificationItem.getData();
        if (data.getIsFollowed() && data.getNotifType().equalsIgnoreCase("7")) {
            viewHolder.tvAddFollowBack.setVisibility(View.GONE);
        } else if(!data.getIsFollowed() && data.getNotifType().equalsIgnoreCase("7")) {
            viewHolder.tvAddFollowBack.setVisibility(View.VISIBLE);
        }

        String firstName = data.getFirstName();
        String lastName = data.getLastName();
        String fullName = firstName + " " + lastName;
        String addFollowBack = "Follow " + fullName + " Back";

        viewHolder.tvAddFollowBack.setText(addFollowBack);
        viewHolder.tvAddFollowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String followUserId = data.getFromUserId();
                setFollow(followUserId, viewHolder.tvAddFollowBack);
            }
        });


    }

    private void executeAction(int actionType, NotificationItem notificationItem) {
        if (actionType == 1) {
            context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", notificationItem.getData().getFromUserId()).putExtra("user_name", notificationItem.getData().getUsername()));
        } else if (actionType == 2) {
            notificationClickListener.onNotificationPostActionClick(notificationItem.getData().getTypeId(), false);
        } else if (actionType == 3) {
            notificationClickListener.onNotificationPostActionClick(notificationItem.getData().getTypeId(), true);
        } else if (actionType == 4) {
            context.startActivity(new Intent(context, StarContributorActivity.class).putExtra("category_id", "").putExtra("category_name", ""));
        } else if (actionType == 5) {
            context.startActivity(new Intent(context, StarContributorActivity.class).putExtra("category_id", notificationItem.getData().getCategoryId()).putExtra("category_name", notificationItem.getData().getCategoryName()));
        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd MMM yyyy hh:mm a", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        ImageView imageNotification;
        TextView notificationDetails, notificationTime, tvAddFollowBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            imageNotification = itemView.findViewById(R.id.imageNotification);
            notificationDetails = itemView.findViewById(R.id.notificationDetails);
            notificationTime = itemView.findViewById(R.id.notificationTime);
            tvAddFollowBack = itemView.findViewById(R.id.tvAddFollowBack);
        }
    }


    private void setFollow(String followUserId, TextView tvAddFollowBack) {
//        progressBarLoading.setVisibility(View.VISIBLE);
        showProgressBar(context.getString(R.string.loading));
        Call<String> call = webService.setFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {

                        tvAddFollowBack.setVisibility(View.GONE);
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                progressBarLoading.setVisibility(View.GONE);
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                progressBarLoading.setVisibility(View.GONE);
                hideProgressBar();
            }
        });
    }

    private void sendBrowserNotification(String followUserId) {
        Call<String> call = webService.sendBrowserNotification(deviceId, userId, token, followUserId, userId, "0", "follow");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void showProgressBar(String title) {
        progressDialog.setMessage(title);
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }
}
