package com.liker.android.Comment.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import com.doodle.App;
//import com.doodle.Comment.model.Comment_;
//import com.doodle.Comment.model.Reply;
//import com.doodle.Home.model.PostItem;
//import com.doodle.R;
//import com.doodle.Tool.PrefManager;

import com.liker.android.App;
import com.liker.android.Comment.model.Comment_;
import com.liker.android.Comment.model.Reply;
import com.liker.android.Group.model.GroupDataInfo;
import com.liker.android.Group.service.GroupAboutDescriptionEvent;
import com.liker.android.Group.service.GroupWebservice;
import com.liker.android.Home.model.PostItem;
import com.liker.android.R;
import com.liker.android.Tool.AppSingleton;
import com.liker.android.Tool.PrefManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.liker.android.Tool.Tools.isEmpty;

//import static com.doodle.Tool.Tools.isEmpty;

public class ReportSendCategorySheet extends BottomSheetDialogFragment implements View.OnClickListener {
    public static String REPLY_key = "reply_key";
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    public static final String COMMENT_key = "comment_key";
    public static final String GROUP_DATA_INFO_key = "group_data_info_key";
    public static final String IS_FOLLOW_key = "is_follow_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    private String reportId;
    private String reportPersonName;
    private boolean isFollow;
    private Comment_ commentItem;
    private GroupDataInfo groupDataInfo;
    List<String> reportCategoryList = new ArrayList<>();
    private String categoryName;
    private String leaveGroup;

    public static ReportSendCategorySheet newInstance(String message, Comment_ comment_Item, boolean isFollow, GroupDataInfo groupDataInfo) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
        args.putParcelable(ReportSendCategorySheet.COMMENT_key, comment_Item);
        args.putParcelable(ReportSendCategorySheet.GROUP_DATA_INFO_key, groupDataInfo);
        args.putString(ReportSendCategorySheet.MESSAGE_key, message);
        args.putBoolean(ReportSendCategorySheet.IS_FOLLOW_key, isFollow);

        ReportSendCategorySheet fragment = new ReportSendCategorySheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        commentItem = new Comment_();
        Bundle argument = getArguments();
        if (argument != null) {
            reportId = argument.getString(MESSAGE_key);
            isFollow = argument.getBoolean(IS_FOLLOW_key);
            commentItem = argument.getParcelable(COMMENT_key);
            groupDataInfo = argument.getParcelable(GROUP_DATA_INFO_key);
        }
    }


    private RadioGroup radioGroupSendCategory;
    private RadioButton radioMessagePerson, radioUnfollowPerson, radioReportLiker;
    private String reoprtUser, unFollowUser, reportLiker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_send_category, container, false);

        root.findViewById(R.id.imgCloseSendCategory).setOnClickListener(this);
        root.findViewById(R.id.btnCategoryContinue).setOnClickListener(this);


        radioGroupSendCategory = (RadioGroup) root.findViewById(R.id.radioGroupSendCategory);

        PostItem item = App.getItem();
        Reply replyItem = App.getReplyItem();
        if (!isEmpty(commentItem)) {
            reportPersonName = commentItem.getUserFirstName() + " " + commentItem.getUserLastName();
        } else if (!isEmpty(item)) {
            reportPersonName = item.getUserFirstName() + " " + item.getUserLastName();
        } else if (!isEmpty(replyItem)) {
            reportPersonName = replyItem.getFirstName() + " " + replyItem.getLastName();
        }else if (!isEmpty(groupDataInfo)){
            reportPersonName="group admin";
        }

        reoprtUser = "Send a message to " + reportPersonName;
        unFollowUser = "Unfollow " + reportPersonName;
        reportLiker = "Report to Liker";
        leaveGroup="Leave group";
        /*Send a message to group admin
Leave group
Report to Liker*/
        if (isFollow) {
            reportCategoryList.add(reoprtUser);
            reportCategoryList.add(unFollowUser);
            reportCategoryList.add(reportLiker);
        } else if(groupDataInfo.isIsMember()) {
            reportCategoryList.add(reoprtUser);
            reportCategoryList.add(leaveGroup);
            reportCategoryList.add(reportLiker);
        }else if(!groupDataInfo.isIsMember()){
            reportCategoryList.add(reoprtUser);
           // reportCategoryList.add(leaveGroup);
            reportCategoryList.add(reportLiker);
        }
        else {
            reportCategoryList.add(reoprtUser);
            reportCategoryList.add(reportLiker);
        }


        for (int i = 0; i < reportCategoryList.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(reportCategoryList.get(i));
            radioButton.setId(i);
            radioGroupSendCategory.addView(radioButton);
        }

        //set listener to radio button group
        radioGroupSendCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = radioGroupSendCategory.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) root.findViewById(checkedRadioButtonId);
                categoryName = radioBtn.getText().toString();
            }
        });


        return root;
    }


    @Override
    public void onClick(View v) {

        int selectedId = radioGroupSendCategory.getCheckedRadioButtonId();
        int id = v.getId();
        switch (id) {
            case R.id.btnCategoryContinue:

                if (reoprtUser.equalsIgnoreCase(categoryName)) {
                    mListener.onPersonLikerClicked(R.drawable.ic_public_black_12dp, categoryName);
                    dismiss();
                } else if (unFollowUser.equalsIgnoreCase(categoryName)) {
                    mListener.onFollowClicked(R.drawable.ic_public_black_12dp, categoryName);
                    dismiss();
                } else if (reportLiker.equalsIgnoreCase(categoryName)) {
                    mListener.onReportLikerClicked(R.drawable.ic_public_black_12dp, categoryName);
                    dismiss();
                }else if(leaveGroup.equalsIgnoreCase(categoryName)){
                    setLeaveMember();
                  //  dismiss();
                }

                break;
            case R.id.imgCloseSendCategory:
                dismiss();
                break;
        }
    }

    private void setLeaveMember() {

        PrefManager manager=new PrefManager(getActivity());
        String deviceId=manager.getDeviceId();
        String userId=manager.getProfileId();
        String token=manager.getToken();
        String groupId=groupDataInfo.getGroupInfo().getGroupId();
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.updating));
        progressDialog.show();
        GroupWebservice groupWebservice=GroupWebservice.retrofitBase.create(GroupWebservice.class);
        Call<String> call = groupWebservice.leaveMembers(deviceId, userId, token, userId, groupId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        // isFollow = false;
                        GroupDataInfo event=new GroupDataInfo(false);
                        EventBus.getDefault().post(event);
                        dismiss();

                        //tvFollow.setText(getString(R.string.follow));
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }

    public interface BottomSheetListener {
        void onFollowClicked(int image, String text);

        void onReportLikerClicked(int image, String text);

        void onPersonLikerClicked(int image, String text);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}

