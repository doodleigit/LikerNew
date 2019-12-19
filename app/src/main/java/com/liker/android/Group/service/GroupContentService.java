package com.liker.android.Group.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.liker.android.App;
import com.liker.android.Authentication.model.Country;
import com.liker.android.Authentication.model.CountryInfo;
import com.liker.android.Authentication.service.AuthService;
import com.liker.android.Group.model.GroupContent;
import com.liker.android.Group.model.GroupContentData;
import com.liker.android.Group.model.Message;
import com.liker.android.Group.model.Success;
import com.liker.android.Tool.PrefManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

/*import com.doodle.Authentication.model.Country;
import com.doodle.Authentication.model.CountryInfo;*/


public class GroupContentService extends IntentService {

    public static final String TAG = "GroupContentService";
    public static final String GROUP_CONTENT_SERVICE_MESSAGE = "groupContentServiceMessage";
    public static final String GROUP_CONTENT_SERVICE_PAYLOAD = "groupContentServicePayload";


    public GroupContentService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GroupWebservice groupWebservice= GroupWebservice.retrofitBase.create(GroupWebservice.class);
        PrefManager manager=new PrefManager(this);
        String deviceId=manager.getDeviceId();
        String userId=manager.getProfileId();
        String token=manager.getToken();

        Call<GroupContent> call = groupWebservice.allGroupInfo(deviceId,userId,token,userId);
        GroupContent groupContent=null;
        try {
            groupContent = call.execute().body();
//            Log.d("groupContent", groupContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
          //  Log.i(TAG, "onHandleIntent: " + e.getMessage());
        }
//        Return the data to GroupContentActivity
        Intent messageIntent = new Intent(GROUP_CONTENT_SERVICE_MESSAGE);
        messageIntent.putExtra(GROUP_CONTENT_SERVICE_PAYLOAD, (Parcelable) groupContent);
        LocalBroadcastManager manager1 = LocalBroadcastManager.getInstance(getApplicationContext());
        manager1.sendBroadcast(messageIntent);

    }
}

