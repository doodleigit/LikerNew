package com.liker.android.Group.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.usage.NetworkStatsManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.R;
import com.liker.android.Tool.AppSingleton;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.liker.android.Tool.Tools.isNullOrEmpty;

public class GroupAboutEditDialog extends DialogFragment implements View.OnClickListener {


    public static final String GROUP_ABOUT_MESSAGE = "group_about_message";
    private String groupAboutMessage;
    private GroupWebservice groupWebservice;
    private PrefManager manager;
    private boolean networkOk;
    private String deviceId, profileUserId, token, userId;


    private EditText editPageAboutDescription;
    private ProgressDialog progressDialog;
    private View view;
    private TextView tvBackGroupAboutPage, tvSaveDescription;
    private String pageDescription;

    public static GroupAboutEditDialog newInstance(String groupAbout) {

        Bundle args = new Bundle();
        args.putString(GROUP_ABOUT_MESSAGE, groupAbout);
        GroupAboutEditDialog fragment = new GroupAboutEditDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        Bundle argument = getArguments();
        if (argument != null) {
            groupAboutMessage = argument.getString(GROUP_ABOUT_MESSAGE);
        }
        groupWebservice = GroupWebservice.retrofitBase.create(GroupWebservice.class);
        progressDialog=new ProgressDialog(getActivity());
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());
        manager = new PrefManager(getActivity());
        deviceId = manager.getDeviceId();
        profileUserId = manager.getProfileId();
        userId = manager.getProfileId();
        token = manager.getToken();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_about_edit_dialog, container, false);
        initialComponent();
        setData();
        return view;
    }

    private void setData() {

        if(groupAboutMessage.equalsIgnoreCase(getString(R.string.add_group_description))){
            editPageAboutDescription.setHint(R.string.add_group_description);
        }else {
            editPageAboutDescription.append(groupAboutMessage);
        }
    }


    private void initialComponent() {
        editPageAboutDescription = view.findViewById(R.id.editPageAboutDescription);
        tvBackGroupAboutPage = view.findViewById(R.id.tvBackGroupAboutPage);
        tvSaveDescription = view.findViewById(R.id.tvSaveDescription);
        tvBackGroupAboutPage.setOnClickListener(this);
        tvSaveDescription.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        pageDescription=editPageAboutDescription.getText().toString();
        int id = v.getId();
        switch (id) {
            case R.id.tvBackGroupAboutPage:
                dismiss();
                break;
            case R.id.tvSaveDescription:
                if(groupAboutMessage.equalsIgnoreCase(pageDescription)){
                    dismiss();
                }else if(isNullOrEmpty(pageDescription)){
                    dismiss();
                }else {
                    if(networkOk){
                        Call<String> call =groupWebservice.updateGroupabout(deviceId,userId,token,profileUserId, AppSingleton.getInstance().getGroupId(),groupAboutMessage);
                        sendRequestUpdateGroup(call);
                    }else {
                        Toast.makeText(getActivity(), "No internet!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void sendRequestUpdateGroup(Call<String> call) {
      call.enqueue(new Callback<String>() {
          @Override
          public void onResponse(Call<String> call, Response<String> response) {
              String jsonResponse = response.body();
              try {
                  JSONObject obj = new JSONObject(jsonResponse);
                  boolean status = obj.getBoolean("status");
                  JSONObject messageObject=obj.getJSONObject("message");
                  JSONObject successObject=messageObject.getJSONObject("success");
                  if (status) {
                      String message=successObject.getString("message");
                      Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                      AppSingleton.getInstance().setPageAboutDescription(pageDescription);
                      dismiss();
                      //  isFollow = false;
//                      isMember = true;
//                      imageGroupJoin.setImageResource(R.drawable.ic_group_joined_24dp);
//                      tvGroupJoin.setText(getString(R.string.groupJoined));
                      //tvFollow.setText(getString(R.string.follow));
                  } else {
                      Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
              progressDialog.dismiss();
          }

          @Override
          public void onFailure(Call<String> call, Throwable t) {

          }
      });
    }
}
