package com.liker.android.Comment.view.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

//import com.doodle.App;
//import com.doodle.Comment.model.Comment_;
//import com.doodle.Comment.service.CommentService;
//import com.doodle.Home.model.PostItem;
//import com.doodle.R;
//import com.doodle.Tool.NetworkHelper;
//import com.doodle.Tool.PrefManager;
//import com.doodle.Tool.Tools;

import com.liker.android.App;
import com.liker.android.Comment.model.Comment_;
import com.liker.android.Comment.service.CommentService;
import com.liker.android.Group.model.GroupDataInfo;
import com.liker.android.Home.model.PostItem;
import com.liker.android.R;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.Comment.view.fragment.ReportSendCategorySheet.GROUP_DATA_INFO_key;
import static com.liker.android.Tool.Tools.isEmpty;
import static com.liker.android.Tool.Tools.isNullOrEmpty;
import static com.liker.android.Tool.Tools.isNullOrWhiteSpace;

//import static com.doodle.Tool.Tools.isEmpty;
//import static com.doodle.Tool.Tools.isNullOrWhiteSpace;

public class ReportLikerMessageSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    public static final String MESSAGE_key = "message_key";
    public static final String COMMENT_key = "comment_key";
    private Comment_ commentItem;
    private GroupDataInfo groupDataInfo;
    private CommentService commentService;

    private String deviceId, profileId, token, userIds;
    private String commentId;
    private String postId;
    private String reportType;
    private Object toId;
    private String groupId;

    public static ReportLikerMessageSheet newInstance(String message, Comment_ commentItem, GroupDataInfo groupDataInfo) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putString(ReportLikerMessageSheet.MESSAGE_key, message);
        args.putParcelable(ReportSendCategorySheet.COMMENT_key, commentItem);
        args.putParcelable(GROUP_DATA_INFO_key, groupDataInfo);
        ReportLikerMessageSheet fragment = new ReportLikerMessageSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private String reasonId, message;
    boolean networkOk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();

        commentService = CommentService.mRetrofit.create(CommentService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());
        commentItem = new Comment_();
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        Bundle argument = getArguments();
        if (argument != null) {
            groupDataInfo = argument.getParcelable(GROUP_DATA_INFO_key);
            commentItem = argument.getParcelable(COMMENT_key);
            reasonId = argument.getString(MESSAGE_key);

        }
    }


    EditText etReportMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_liker_message, container, false);

        root.findViewById(R.id.imgCloseReason).setOnClickListener(this);
        root.findViewById(R.id.btnReasonContinue).setOnClickListener(this);
        etReportMessage = root.findViewById(R.id.etReportMessage);

        return root;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btnReasonContinue:
                if (networkOk) {
                    if(!isEmpty(commentItem)){
                        commentId = commentItem.getId();
                        postId = commentItem.getPostId();
                        reportType="3";
                    }else if (!isEmpty(groupDataInfo)) {
                        commentId = "";
                        toId = groupDataInfo.getGroupInfo().getCreatorId();
                        postId = "";
                        reportType = "5";
                        groupId=groupDataInfo.getGroupInfo().getGroupId();
                        /*user_id: 28738
                        to_id: 26444
                        msg: sdf sf sf
                        post_id: ''
                        reasonid: 5
                        comment_id:
                        group_id: 2
                        report_type: 5
                        send_type: 1*/

                    }

                    else {
                        PostItem item=new PostItem();
                        item=App.getItem();
                        commentId="";
                        postId=item.getPostId();
                        if(!isNullOrEmpty(postId)){
                            reportType="2";

                        }else {
                            reportType="1";

                        }
                    }
                    message = etReportMessage.getText().toString();
                    if(!isNullOrWhiteSpace(message)){
                        Call<String> call = commentService.reportUser(deviceId, profileId, token, commentId, message, postId, reasonId, reportType,groupId, "2", "20248", userIds);
                        sendReportUserRequest(call);
                    }else {
                        Tools.toast(getActivity(),"Message Required!",R.drawable.icon_checked);
                    }

                    // delayLoadComment(mProgressBar);
                } else {
                    Tools.showNetworkDialog(getActivity().getSupportFragmentManager());

                }
                break;
            case R.id.imgCloseReason:
                dismiss();
                break;
        }


    }

    private void sendReportUserRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");

                            if (status) {
                                // adapter.notifyDataSetChanged();
                                Tools.toast(getActivity(),"your message was successfully sent",R.drawable.icon_checked);
                                dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("message",t.getMessage());
            }
        });
    }

    public interface BottomSheetListener {
        void onReportLikerMessageClicked(int image, String text);
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

