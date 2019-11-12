package com.liker.android.Post.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liker.android.App;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.doodle.R;
//import com.doodle.Tool.Tools;


public class FollowStatus extends DialogFragment {
    private final String TAG = "AUC_CUSTOM";
    private FollowStatusListener mHost;
    private Parcelable bitmap;
    private String messageFollow;
    private String userProfileImage;

    // TODO: Implement an interface for hosts to get callbacks
    public interface FollowStatusListener {
        public void onFollowResult(DialogFragment dlg,PostItem postItem,int position);

        public void onUnFollowResult(DialogFragment dlg,PostItem postItem,int position);

        public void onNeutralResult(DialogFragment dlg);
    }


    public static final String POST_ITEM_key = "post_item_key";
    public static final String POST_ITEM_POSITION_key = "post_item_position_key";


    public static FollowStatus newInstance(PostItem postItem,int position) {

        Bundle args = new Bundle();
        args.putParcelable(FollowStatus.POST_ITEM_key, postItem);
        args.putInt(FollowStatus.POST_ITEM_POSITION_key, position);


        FollowStatus fragment = new FollowStatus();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    String message;
    PostItem mPostItem;
    int mPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle argument = getArguments();
        if (argument != null) {
//            UserInfo data = argument.getParcelable(MESSAGE_key);
            PostItem postItem = argument.getParcelable(POST_ITEM_key);
            mPostItem = postItem;
            mPosition=argument.getInt(POST_ITEM_POSITION_key);
            String userFullName = String.format("%s %s", postItem.getUserFirstName(), postItem.getUserLastName());
            String categoryName = postItem.getCatName();
            userProfileImage = postItem.getUesrProfileImg();
            String userName = postItem.getPostUsername();
            String userId = postItem.getPostUserid();
            messageFollow = userFullName + " is a Star Contributor to the " + categoryName + " category.";
            Log.d(TAG, "onCreateView: " + message);
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // TODO: Create the custom layout using the LayoutInflater class
        // LayoutInflater inflater = LayoutInflater.from(getActivity()); //or
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.follow_status, null);

        CircleImageView imagePostUser = view.findViewById(R.id.imagePostUser);
        String userImageUrl = AppConstants.PROFILE_IMAGE + userProfileImage;
        Glide.with(App.getAppContext())
                .load(userImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(imagePostUser);

        // Add URL in text.

        TextView tvContributorStatus = view.findViewById(R.id.tvContributorStatus);
        //  Tools.textLinkup(messageFollow,"https://www.liker.com/trending", tvContributorStatus);
        tvContributorStatus.setText(getFollowSpannableStringBuilder(getActivity(), mPostItem));
        tvContributorStatus.setMovementMethod(LinkMovementMethod.getInstance());
        // TODO: Build the dialog
        builder
                .setPositiveButton("Follow "+mPostItem.getUserFirstName(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHost.onFollowResult(FollowStatus.this,mPostItem,mPosition);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Cancel clicked");
                        mHost.onUnFollowResult(FollowStatus.this,mPostItem,mPosition);
                    }
                }).setView(view);

        return builder.create();
    }


    // TODO: Listen for cancel message by overriding onCancel

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG, "Dialog cancelled!");
    }


    // TODO: Override onAttach to get Activity instance


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = (FollowStatusListener) context;
    }

    public SpannableStringBuilder getFollowSpannableStringBuilder(Context context, PostItem postItem) {

        String userFullName = String.format("%s ", postItem.getUserFirstName());
        String categoryName = postItem.getCatName();
        String userName = postItem.getPostUsername();
        String userId = postItem.getPostUserid();
        String messageFollow = " is a Star Contributor to the " + categoryName + " category.";

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableUser = new SpannableString(userFullName);

        ClickableSpan clickableSpanUser = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", userId).putExtra("user_name", userName));
                dismiss();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        spannableUser.setSpan(clickableSpanUser, 0, userFullName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.parseColor("#60b2fc"));
        spannableUser.setSpan(foregroundColorSpan1, 0, userFullName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.append(spannableUser);

        SpannableString spannableWallInfo = new SpannableString(messageFollow);
        builder.append(spannableWallInfo);


        return builder;

    }
}
