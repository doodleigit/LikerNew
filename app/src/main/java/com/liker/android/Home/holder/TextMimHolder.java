package com.liker.android.Home.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
/*import com.doodle.App;
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.view.activity.CommentPost;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Home.model.PostFooter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.SharedProfile;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.view.activity.EditPost;
import com.doodle.Home.view.activity.Home;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.Home.view.fragment.LikerUserListFragment;
import com.doodle.Home.view.fragment.PostPermissionSheet;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.Post.view.activity.PostPopup;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.Operation;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;*/
import com.liker.android.App;
import com.liker.android.Comment.model.Comment;
import com.liker.android.Comment.model.CommentItem;
import com.liker.android.Comment.model.Comment_;
import com.liker.android.Comment.model.MentionItem;
import com.liker.android.Comment.model.Reason;
import com.liker.android.Comment.model.ReportReason;
import com.liker.android.Comment.service.CommentService;
import com.liker.android.Comment.view.activity.CommentPost;
import com.liker.android.Comment.view.fragment.BlockUserDialog;
import com.liker.android.Comment.view.fragment.ReportReasonSheet;
import com.liker.android.Home.model.PostFooter;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Home.model.PostTextIndex;
import com.liker.android.Home.model.SharedProfile;
import com.liker.android.Home.service.HomeService;
import com.liker.android.Home.view.activity.Home;
import com.liker.android.Home.view.activity.PostShare;
import com.liker.android.Home.view.fragment.LikerUserListFragment;
import com.liker.android.Home.view.fragment.PostPermissionSheet;
import com.liker.android.Post.model.Mim;
import com.liker.android.Post.service.DataProvider;
import com.liker.android.Post.view.activity.PostPopup;
import com.liker.android.Post.view.fragment.FollowStatus;
import com.liker.android.Profile.view.ProfileActivity;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.Operation;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.doodle.Tool.AppConstants.FACEBOOK_SHARE;
//import static com.doodle.Tool.Tools.containsIllegalCharacters;
//import static com.doodle.Tool.Tools.delayLoadComment;
//import static com.doodle.Tool.Tools.dismissDialog;
//import static com.doodle.Tool.Tools.getSpannableStringBuilder;
//import static com.doodle.Tool.Tools.getSpannableStringShareHeader;
//import static com.doodle.Tool.Tools.getWallSpannableStringBuilder;
//import static com.doodle.Tool.Tools.isNullOrEmpty;
//import static com.doodle.Tool.Tools.readMoreText;
//import static com.doodle.Tool.Tools.sendNotificationRequest;
//import static com.doodle.Tool.Tools.setMargins;
//import static com.doodle.Tool.Tools.showBlockUser;
import static com.liker.android.Tool.AppConstants.FACEBOOK_SHARE;
import static com.liker.android.Tool.Tools.containsIllegalCharacters;
import static com.liker.android.Tool.Tools.delayLoadComment;
import static com.liker.android.Tool.Tools.extractMentionText;
import static com.liker.android.Tool.Tools.extractUrls;
import static com.liker.android.Tool.Tools.fadeInFadeOutFollow;
import static com.liker.android.Tool.Tools.followToggle;
import static com.liker.android.Tool.Tools.getFollowSpannableStringBuilder;
import static com.liker.android.Tool.Tools.getSpannableStringBuilder;
import static com.liker.android.Tool.Tools.getSpannableStringShareHeader;
import static com.liker.android.Tool.Tools.getWallSpannableStringBuilder;
import static com.liker.android.Tool.Tools.isNullOrEmpty;
import static com.liker.android.Tool.Tools.readMoreText;
import static com.liker.android.Tool.Tools.setMargins;
import static java.lang.Integer.parseInt;

public class TextMimHolder extends RecyclerView.ViewHolder {

    public static final String ITEM_KEY = "item_key";
    public static final String POST_ITEM_POSITION = "post_item_position";
    public TextView tvHeaderInfo, tvPostTime, tvPostUserName, tvImgShareCount, tvPostLikeCount, tvLinkScriptText, tvCommentCount, tvWallPostInfo;
    public CircleImageView imagePostUser;
    //    public ReadMoreTextView tvPostContent;
    public TextView tvPostContent;
    public EmojiTextView tvPostEmojiContent;
    public ImageView star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    public LinearLayout postBodyLayer, sharePostBody;
    private Drawable mDrawable;
    List<Mim> viewColors = new DataProvider().getMimList();

    PostItem item;
    private PopupMenu popup, popupMenu;
    private ImageView imagePostShare, imagePostShareSetting, imagePermission;
    public HomeService webService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private Context mContext;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    public ImageView imagePostPermission;
    //Comment
    Comment commentItem;
    private List<Comment_> comments = new ArrayList<Comment_>();
    private String commentPostId;
    RelativeLayout commentHold;
    private String commentText, commentUserName, commentUserImage, commentImage, commentTime;
    public EmojiTextView tvCommentMessage;
    public ImageView imagePostCommenting, imageCommentLikeThumb;
    public CircleImageView imageCommentUser;
    public TextView tvCommentUserName, tvCommentTime, tvCommentLike, tvCommentReply, tvCountCommentLike;
    private String userPostId;
    private PopupMenu popupCommentMenu;


    //SHOW ALL COMMENTS
    private CommentService commentService;
    private LinearLayout commentContainer;
    int limit = 10;
    int offset = 0;
    boolean networkOk;
    private ProgressBar mProgressBar;
    public ImageView imagePostComment;
    LinearLayout commentBox;
    public static final String COMMENT_KEY = "comment_item_key";
    private boolean notificationOff;
    private String postPermissions;
    private boolean notificationStatus;

    //Delete post
    public PostItemListener listener;
    private String postLike;
    private int postTotalShare;

    //SHARE-POST-HEADER
    private String sharedFirstName;
    private String sharedLastName;
    private String sharedFullName;
    private String isShared;
    private String sharedDateTime;
    private String sharedUserProfileImage;
    private int sharedTotalStar;
    private String sharedPostPermission;
    private String sharedUserProfileLike;
    private String sharedPostText;
    private String sharedCategoryName;
    private String className;

    private LinearLayout containerHeaderShare;
    private CircleImageView imageSharePostUser;
    private ImageView imageSharePostPermission;
    private TextView tvSharePostUserName, tvSharePostTime, tvShareHeaderInfo, tvSharePostContent;
    private TextView tvShared, tvPostShareUserName;
    private MediaPlayer player;

    //footerFollow Status
    private ViewGroup contentFollow,layoutFollowUser,rootView;
    private CircleImageView imageFollowUser;
    private TextView tvContributorStatus,tvFollowUserName;
    private ImageView unFollowImage;
    private String userFollowProfileImage;

    public interface PostItemListener {
        void deletePost(PostItem postItem, int position);

    }

    public ImageView imgLike;
    private int postLikeNumeric;
    ViewGroup tvLikeShare;
    private ProgressDialog progressDialog;

    public TextMimHolder(View itemView, Context context, PostItemListener mimListener, String className) {
        super(itemView);
        mContext = context;
        this.listener = mimListener;
        this.className = className;

        player = MediaPlayer.create(mContext, R.raw.post_like);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog((Activity) context);
        manager = new PrefManager(App.getAppContext());
        progressDialog = new ProgressDialog(mContext);
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);
        tvPostUserName = (TextView) itemView.findViewById(R.id.tvPostUserName);
        imagePostUser = (CircleImageView) itemView.findViewById(R.id.imagePostUser);
        tvHeaderInfo = (TextView) itemView.findViewById(R.id.tvHeaderInfo);
        tvImgShareCount = (TextView) itemView.findViewById(R.id.tvImgShareCount);
        tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);
        tvPostLikeCount = (TextView) itemView.findViewById(R.id.tvPostLikeCount);
        //tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);
        tvPostContent =itemView.findViewById(R.id.tvPostContent);
        tvLinkScriptText = (ReadMoreTextView) itemView.findViewById(R.id.tvLinkScriptText);
        tvPostEmojiContent = (EmojiTextView) itemView.findViewById(R.id.tvPostEmojiContent);
        postBodyLayer = (LinearLayout) itemView.findViewById(R.id.postBodyLayer);
        sharePostBody = (LinearLayout) itemView.findViewById(R.id.sharePostBody);
        tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
        imgLike = itemView.findViewById(R.id.imgLike);
        tvLikeShare = itemView.findViewById(R.id.tvLikeShare);
        tvWallPostInfo = itemView.findViewById(R.id.tvWallPostInfo);


        star1 = itemView.findViewById(R.id.star1);
        star2 = itemView.findViewById(R.id.star2);
        star3 = itemView.findViewById(R.id.star3);
        star4 = itemView.findViewById(R.id.star4);
        star5 = itemView.findViewById(R.id.star5);
        star6 = itemView.findViewById(R.id.star6);
        star7 = itemView.findViewById(R.id.star7);
        star8 = itemView.findViewById(R.id.star8);
        star9 = itemView.findViewById(R.id.star9);
        star10 = itemView.findViewById(R.id.star10);
        star11 = itemView.findViewById(R.id.star11);
        star12 = itemView.findViewById(R.id.star12);
        star13 = itemView.findViewById(R.id.star13);
        star14 = itemView.findViewById(R.id.star14);
        star15 = itemView.findViewById(R.id.star15);
        star16 = itemView.findViewById(R.id.star16);
        imagePostShare = itemView.findViewById(R.id.imagePostShare);
        imagePostShareSetting = itemView.findViewById(R.id.imagePostShareSetting);
        imagePermission = itemView.findViewById(R.id.imagePermission);
        imagePostPermission = itemView.findViewById(R.id.imagePostPermission);


        //Comment
        tvCommentMessage = itemView.findViewById(R.id.tvCommentMessage);
        commentHold = (RelativeLayout) itemView.findViewById(R.id.commentHold);
        imagePostCommenting = itemView.findViewById(R.id.imagePostCommenting);
        imageCommentLikeThumb = itemView.findViewById(R.id.imageCommentLikeThumb);

        imageCommentUser = itemView.findViewById(R.id.imageCommentUser);
        tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
        tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
        tvCommentLike = itemView.findViewById(R.id.tvCommentLike);
        tvCommentReply = itemView.findViewById(R.id.tvCommentReply);
        tvCountCommentLike = itemView.findViewById(R.id.tvCountCommentLike);
        imageCommentLikeThumb.setVisibility(View.GONE);
        tvCountCommentLike.setVisibility(View.GONE);

        //ALL Comment
        imagePostComment = (ImageView) itemView.findViewById(R.id.imagePostComment);
        commentService = CommentService.mRetrofit.create(CommentService.class);
        networkOk = NetworkHelper.hasNetworkAccess(mContext);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.ProgressBar);
        commentContainer = itemView.findViewById(R.id.commentContainer);

        //SHARE_POST_HEADER
        containerHeaderShare = itemView.findViewById(R.id.containerHeaderShare);
        imageSharePostUser = itemView.findViewById(R.id.imageSharePostUser);
        tvSharePostUserName = itemView.findViewById(R.id.tvSharePostUserName);
        tvSharePostTime = itemView.findViewById(R.id.tvSharePostTime);
        tvShareHeaderInfo = itemView.findViewById(R.id.tvShareHeaderInfo);

        imageSharePostPermission = itemView.findViewById(R.id.imageSharePostPermission);
        tvSharePostContent = itemView.findViewById(R.id.tvSharePostContent);
        tvShared = itemView.findViewById(R.id.tvShared);
        tvPostShareUserName = itemView.findViewById(R.id.tvPostShareUserName);

        contentFollow = itemView.findViewById(R.id.contentFollow);
        rootView = itemView.findViewById(R.id.main_activity_root_view);
        layoutFollowUser = itemView.findViewById(R.id.layoutFollowUser);
        tvContributorStatus = itemView.findViewById(R.id.tvContributorStatus);
        tvFollowUserName = itemView.findViewById(R.id.tvFollowUserName);
        imageFollowUser = itemView.findViewById(R.id.imageFollowUser);
        unFollowImage = itemView.findViewById(R.id.unFollowImage);

    }

    AppCompatActivity activity;
    int position;
    RecyclerView.ViewHolder viewHolder;

    public void setItem(PostItem item, int position, RecyclerView.ViewHolder viewHolder) {
        this.item = item;
        this.position = position;
        this.viewHolder = viewHolder;
        userPostId = item.getPostId();

        String postPermission = item.getPermission();

        switch (postPermission) {
            case "0":
                imagePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
                break;
            case "1":
                imagePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
                break;
            case "2":
                imagePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
                break;
        }


        isShared = item.getIsShared();
        if (App.isSharePostfooter()) {
            tvLikeShare.setVisibility(View.GONE);
            imagePermission.setVisibility(View.GONE);
        } else {
            tvLikeShare.setVisibility(View.VISIBLE);
            imagePermission.setVisibility(View.VISIBLE);
        }

        if ("1".equalsIgnoreCase(isShared)) {
            containerHeaderShare.setVisibility(View.VISIBLE);
            //  imagePermission.setVisibility(View.GONE);

            setMargins(postBodyLayer, 10, 10, 10, 10);
            postBodyLayer.setBackgroundResource(R.drawable.drawable_comment);
            sharePostBody.setBackgroundColor(Color.parseColor("#cfcfcf"));

            SharedProfile itemSharedProfile = item.getSharedProfile();
            sharedFirstName = itemSharedProfile.getUserFirstName();
            sharedLastName = itemSharedProfile.getUserLastName();
            sharedFullName = sharedFirstName + " " + sharedLastName;
            sharedDateTime = itemSharedProfile.getDateTime();
            sharedUserProfileImage = itemSharedProfile.getUesrProfileImg();
            sharedTotalStar = Integer.parseInt(itemSharedProfile.getUserGoldStars()) + Integer.parseInt(itemSharedProfile.getUserSilverStars());
            sharedPostPermission = itemSharedProfile.getPermission();
            sharedUserProfileLike = itemSharedProfile.getUserProfileLikes();
            sharedPostText = item.getSharedPostText();
            sharedCategoryName = item.getCatName();
            SpannableStringBuilder builder = getSpannableStringShareHeader(sharedUserProfileLike, "", sharedTotalStar, sharedCategoryName);
            long myMillis = Long.parseLong(sharedDateTime) * 1000;
            String postDate = Operation.postDateCompare(mContext, myMillis);
            //    tvSharePostTime.setText(chatDateCompare(mContext,myMillis));
            tvSharePostUserName.setText(sharedFullName);
            tvShareHeaderInfo.setText(builder);
            tvSharePostTime.setText(postDate);
          //  tvSharePostContent.setText(sharedPostText);
            readMoreText(mContext,tvSharePostContent,sharedPostText);
            if (sharedPostText == null || sharedPostText.isEmpty()) {
                tvSharePostContent.setVisibility(View.GONE);
            } else {
                tvSharePostContent.setVisibility(View.VISIBLE);
            }

            switch (sharedPostPermission) {
                case "0":
                    imageSharePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
                    break;
                case "1":
                    imageSharePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
                    break;
                case "2":
                    imageSharePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
                    break;
            }

        } else {
            containerHeaderShare.setVisibility(View.GONE);
            imagePermission.setVisibility(View.VISIBLE);
            if (App.isSharePostfooter()) {
                imagePermission.setVisibility(View.GONE);
            } else {
                imagePermission.setVisibility(View.VISIBLE);
            }
            if (sharedPostText == null || sharedPostText.isEmpty()) {
                tvSharePostContent.setVisibility(View.GONE);
            } else {
                tvSharePostContent.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostPopup.class);
                intent.putExtra(ITEM_KEY, (Parcelable) item);
                intent.putExtra("has_footer", true);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
            }
        });

        tvCommentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvCountCommentLike.getText().toString().isEmpty()) {
                    imageCommentLikeThumb.setVisibility(View.VISIBLE);
                    tvCountCommentLike.setVisibility(View.VISIBLE);
                    tvCountCommentLike.setText("1");
                } else {
                    tvCountCommentLike.setText("");
                    imageCommentLikeThumb.setVisibility(View.GONE);
                    tvCountCommentLike.setVisibility(View.GONE);
                }
            }
        });


        String text = item.getPostText();
        String contentUrl = FACEBOOK_SHARE + item.getSharedPostId();
        if (containsIllegalCharacters(text)) {
            tvPostContent.setVisibility(View.GONE);
            tvPostEmojiContent.setVisibility(View.VISIBLE);
            tvPostEmojiContent.setText(item.getPostText());
            readMoreText(mContext,tvPostEmojiContent,text);

        } else {
            tvPostEmojiContent.setVisibility(View.GONE);
            tvPostContent.setVisibility(View.VISIBLE);
            readMoreText(mContext,tvPostContent,text);
        }

        int mimId = Integer.parseInt(item.getHasMeme());
        for (Mim temp : viewColors) {
            int getId = temp.getId() == 1 ? 0 : temp.getId();
            if (mimId == getId && mimId > 0) {
                String mimColor = temp.getMimColor();
                if (mimColor.startsWith("#")) {
                    postBodyLayer.setBackgroundColor(Color.parseColor(mimColor));
                    ViewGroup.LayoutParams params = postBodyLayer.getLayoutParams();

                    params.height = (int) mContext.getResources().getDimension(R.dimen._220sdp);
                    postBodyLayer.setLayoutParams(params);
                    postBodyLayer.setGravity(Gravity.CENTER);
                    tvPostContent.setGravity(Gravity.CENTER);
                    tvPostEmojiContent.setGravity(Gravity.CENTER);
                    tvPostContent.setTextAppearance(App.getAppContext(), android.R.style.TextAppearance_Large);
                    tvPostEmojiContent.setTextAppearance(App.getAppContext(), android.R.style.TextAppearance_Large);
                    tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                    tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                } else {

                    String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                    Picasso.with(App.getInstance()).load(imageUrl).into(target);
                    postBodyLayer.setGravity(Gravity.CENTER);
                    tvPostContent.setGravity(Gravity.CENTER);
                    tvPostEmojiContent.setGravity(Gravity.CENTER);
                    postBodyLayer.setBackground(mDrawable);
                    tvPostContent.setHeight((int) mContext.getResources().getDimension(R.dimen._200sdp));
                    tvPostEmojiContent.setHeight((int) mContext.getResources().getDimension(R.dimen._200sdp));
                    switch (mimColor) {
                        case "img_bg_birthday.png":
                            tvPostContent.setTextColor(Color.parseColor("#000000"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#000000"));
                            break;
                        case "img_bg_love.png":
                            tvPostContent.setTextColor(Color.parseColor("#2D4F73"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#2D4F73"));
                            break;
                        case "img_bg_love2.png":
                            tvPostContent.setTextColor(Color.parseColor("#444748"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#444748"));
                            break;
                        case "img_bg_red.png":
                            tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                        case "img_bg_love3.png":
                            tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                    }
                }
            }
        }
        String likes = item.getUserProfileLikes();
        String followers = item.getUserTotalFollowers();
        int silverStar = parseInt(item.getUserSilverStars());
        int goldStar = parseInt(item.getUserGoldStars());

        if (silverStar > 8) {
            silverStar = 8;
        }
        switch (silverStar) {
            case 1:
                star9.setVisibility(View.VISIBLE);
                break;
            case 2:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                break;
            case 3:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                break;
            case 4:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                break;
            case 5:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                break;
            case 6:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                break;
            case 7:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                star15.setVisibility(View.VISIBLE);
                break;
            case 8:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                star15.setVisibility(View.VISIBLE);
                star16.setVisibility(View.VISIBLE);
                break;
            case 0:
                star9.setVisibility(View.GONE);
                star10.setVisibility(View.GONE);
                star11.setVisibility(View.GONE);
                star12.setVisibility(View.GONE);
                star13.setVisibility(View.GONE);
                star14.setVisibility(View.GONE);
                star15.setVisibility(View.GONE);
                star16.setVisibility(View.GONE);
                break;

        }
        if (goldStar > 8) {
            goldStar = 8;
        }
        switch (goldStar) {
            case 1:
                star1.setVisibility(View.VISIBLE);
                break;
            case 2:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                break;
            case 3:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                break;
            case 4:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                break;
            case 5:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                break;
            case 6:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                break;
            case 7:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                star7.setVisibility(View.VISIBLE);
                break;
            case 8:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                star7.setVisibility(View.VISIBLE);
                star8.setVisibility(View.VISIBLE);
                break;
            case 0:
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.GONE);
                star6.setVisibility(View.GONE);
                star7.setVisibility(View.GONE);
                star8.setVisibility(View.GONE);
                break;

        }

        int totalStars = silverStar + goldStar;
        String categoryName = item.getCatName();
        int postSource=item.getPostSource();
        SpannableStringBuilder builder = getSpannableStringBuilder(mContext, item.getCatId(), likes, followers, totalStars, categoryName,postSource);


        if (!isNullOrEmpty(item.getPostWallFirstName())) {

            tvWallPostInfo.setVisibility(View.VISIBLE);
            String postWallUserId = item.getPostWallUserid();
            String postWallUserName = item.getPostWallUsername();
            String postUserId = item.getPostUserid();
            String postUserName = item.getPostUsername();
            String postWalFullName = item.getPostWallFirstName() + " " + item.getPostWallLastName();
            String postUserFullName = String.format("%s %s", item.getUserFirstName(), item.getUserLastName());

            setMargins(tvWallPostInfo, 5, 5, 5, 5);
            // tvWallPostInfo.setBackgroundResource(R.drawable.drawable_comment);
            tvWallPostInfo.setBackgroundColor(Color.parseColor("#80E9ECF5"));
            tvWallPostInfo.setText(getWallSpannableStringBuilder(mContext, postUserFullName, postUserId, postUserName, postWalFullName, postWallUserId, postWallUserName));
            tvWallPostInfo.setMovementMethod(LinkMovementMethod.getInstance());

        } else {
            tvWallPostInfo.setVisibility(View.GONE);
        }

        if ("1".equalsIgnoreCase(isShared)) {
            tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
            tvShared.setVisibility(View.VISIBLE);
            tvPostShareUserName.setText(String.format("%s  %s %s", sharedFullName, "'s", " post"));
        } else {
            tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
        }

        //  tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
        long myMillis = Long.parseLong(item.getDateTime()) * 1000;
        String postDate = Operation.postDateCompare(mContext, myMillis);
        tvPostTime.setText(postDate);
        tvHeaderInfo.setText(builder);
        tvHeaderInfo.setMovementMethod(LinkMovementMethod.getInstance());
        if (!isNullOrEmpty(item.getTotalComment()) && !"0".equalsIgnoreCase(item.getTotalComment())) {
            tvCommentCount.setText(item.getTotalComment());
        } else {
            tvCommentCount.setText("");
        }

        PostFooter postFooter = item.getPostFooter();
        postLike = postFooter.getPostTotalLike();
        postTotalShare = postFooter.getPostTotalShare();
        tvImgShareCount.setText(String.valueOf(postTotalShare));
        if ("0".equalsIgnoreCase(postLike)) {
            tvPostLikeCount.setVisibility(View.GONE);
        } else {
            SpannableString content = new SpannableString(postLike);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tvPostLikeCount.setVisibility(View.VISIBLE);
            tvPostLikeCount.setText(content);
        }

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userIds.equalsIgnoreCase(item.getPostUserid())) {
                    Tools.toast(mContext, "On Liker, you can't like your own posts. That would be cheating ", R.drawable.ic_like_status);
                } else {
                    PostFooter postFooters = item.getPostFooter();
                    if (postFooters.isLikeUserStatus()) {
                        Call<String> call = webService.postUnlike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                        sendPostUnLikeRequest(call);
                    } else {
                        Call<String> call = webService.postLike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                        sendPostLikeRequest(call);
                    }

                    if (!postFooters.isFollowed()) {
                        followToggle(rootView,contentFollow,true);
                        userFollowProfileImage = item.getUesrProfileImg();
                        tvFollowUserName.setText("Follow "+item.getUserFirstName());
                        tvContributorStatus.setText(getFollowSpannableStringBuilder(mContext, item));
                        tvContributorStatus.setMovementMethod(LinkMovementMethod.getInstance());
                        String userImageUrl = AppConstants.PROFILE_IMAGE + userFollowProfileImage;
                        Glide.with(App.getAppContext())
                                .load(userImageUrl)
                                .centerCrop()
                                .dontAnimate()
                                .into(imageFollowUser);

                    }else {
                        followToggle(rootView,contentFollow,false);
                    }

                }
            }
        });


        String userImageUrl = AppConstants.PROFILE_IMAGE + item.getUesrProfileImg();
        Glide.with(App.getAppContext())
                .load(userImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(imagePostUser);
        String shareUserImageUrl = AppConstants.PROFILE_IMAGE + sharedUserProfileImage;
        Glide.with(App.getAppContext())
                .load(shareUserImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(imageSharePostUser);

        if (item.getPostFooter().isLikeUserStatus()) {
            imgLike.setImageResource(R.drawable.like_done);
        } else {
            imgLike.setImageResource(R.drawable.like_normal);
        }

        tvPostLikeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft;
                Fragment prev;
                if (className.equals(AppConstants.WALL)) {
                    ft = ((ProfileActivity) mContext).getSupportFragmentManager().beginTransaction();
                    prev = ((ProfileActivity) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
                } else {
                    ft = ((Home) mContext).getSupportFragmentManager().beginTransaction();
                    prev = ((Home) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
                }
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new LikerUserListFragment();

                Bundle bundle = new Bundle();
                bundle.putString("type_id", item.getIsShared().equals("0") ? item.getPostId() : item.getSharedPostId());
                bundle.putString("total_likes", item.getPostFooter().getPostTotalLike());
                bundle.putString("liker_type", "post");
                dialogFragment.setArguments(bundle);

                dialogFragment.show(ft, "dialog");
            }
        });

        postBodyLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostPopup.class);
                intent.putExtra(ITEM_KEY, (Parcelable) item);
                intent.putExtra("has_footer", true);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
            }
        });

        tvPostUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", item.getPostUserid()).putExtra("user_name", item.getPostUsername()));
            }
        });

        imagePostUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", item.getPostUserid()).putExtra("user_name", item.getPostUsername()));
            }
        });

        imagePostShare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.share_menu, popup.getMenu());


                if (App.isIsPostShare()) {
                    postTotalShare++;
                    popup.getMenu().add(1, R.id.shareAsPost, 1, "Share as a Post (" + postTotalShare + ")").setIcon(R.drawable.like_done);
                } else {
                    popup.getMenu().add(1, R.id.shareAsPost, 1, "Share as a Post (" + postTotalShare + ")").setIcon(R.drawable.like_normal);
                }


//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popup.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.shareAsPost) {
                            String postId = item.getSharedPostId();
                            Call<PostItem> call = webService.getPostDetail(deviceId, profileId, token, userIds, postId);

                            //     Call<PostShareItem> call = webService.getPostDetails(deviceId, profileId, token, userIds, postId);
                            sendShareItemRequest(call);

                        }


                        if (id == R.id.shareFacebook) {

                            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                                @Override
                                public void onSuccess(Sharer.Result result) {

                                    Toast.makeText(mContext, "Share successFull", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(mContext, "Share cancel", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(FacebookException error) {
                                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                            if (!isNullOrEmpty(contentUrl)) {
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse(contentUrl))
                                        .setQuote("")
                                        .build();
                                if (ShareDialog.canShow(ShareLinkContent.class)) {

                                    shareDialog.show(linkContent);
                                }
                            }


                        }
                        if (id == R.id.shareTwitter) {
                            String url = "http://www.twitter.com/intent/tweet?url=" + contentUrl + "&text=" + text;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            mContext.startActivity(i);
                        }

                        if (id == R.id.copyLink) {

                            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied Link", contentUrl);
                            clipboard.setPrimaryClip(clip);
                        }
                        return true;
                    }
                });

            }
        });
        imagePostShareSetting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                activity = (AppCompatActivity) v.getContext();
                PostPermissionSheet reportReasonSheet = PostPermissionSheet.newInstance(item, position);
                reportReasonSheet.show(activity.getSupportFragmentManager(), "ReportReasonSheet");

            }
        });

        imagePermission.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {


                activity = (AppCompatActivity) v.getContext();
                PostPermissionSheet reportReasonSheet = PostPermissionSheet.newInstance(item, position);
                reportReasonSheet.show(activity.getSupportFragmentManager(), "ReportReasonSheet");

        /*        boolean isNotificationOff = item.isIsNotificationOff();
                String postUserId = item.getPostUserid();
                popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.post_permission_menu, popupMenu.getMenu());


                if (userIds.equalsIgnoreCase(postUserId)) {
                    popupMenu.getMenu().findItem(R.id.blockedUser).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.reportedPost).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.publics).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.friends).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.onlyMe).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(true);
                    // popupMenu.getMenu().findItem(R.id.turnOffNotification).setVisible(true);
                } else {
                    popupMenu.getMenu().findItem(R.id.blockedUser).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.reportedPost).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.publics).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.friends).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.onlyMe).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    // popupMenu.getMenu().findItem(R.id.turnOffNotification).setVisible(true);
                }

                if (App.isNotificationStatus()) {

                    if (notificationOff) {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);
                    } else {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

                    }


                } else {
                    if (isNotificationOff) {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);

                    } else {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

                    }
                }


//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popupMenu.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        postPermissions = menuItem.getTitle().toString();
                        if (id == R.id.blockedUser) {
                            if (!((Activity) mContext).isFinishing()) {
                                App.setItem(item);
                                showBlockUser(v);
                            } else {
                                dismissDialog();
                            }
                        }
                        if (id == R.id.reportedPost) {
                            App.setItem(item);
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, item.getPostUserid(), "2", userIds);
                                sendReportReason(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.publics) {

                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "0", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }


                        }
                        if (id == R.id.friends) {
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "2", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.onlyMe) {
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "1", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }

                        }

                        if (id == R.id.edit) {
                            Intent intent = new Intent(mContext, EditPost.class);
                            App.setPosition(position);
                            intent.putExtra(ITEM_KEY, (Parcelable) item);
                            intent.putExtra("position", position);
                            mContext.startActivity(intent);
                            ((Activity) mContext).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                        }
                        if (id == R.id.delete) {

                            listener.deletePost(item, position);
                        }
                        if (id == R.id.turnOffNotification) {
                            activity = (AppCompatActivity) v.getContext();

                            switch (postPermissions) {
                                case "Turn off notifications":
                                    notificationOff = true;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOff(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(activity.getSupportFragmentManager());
                                    }
                                    break;
                                case "Turn on notifications":
                                    notificationOff = false;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOn(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(activity.getSupportFragmentManager());
                                    }
                                    break;

                            }
                        }
                        return true;
                    }
                });*/

            }
        });


        commentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                //  mContext.startActivity(new Intent(mContext, CommentPost.class));
//                FullBottomSheetDialogFragment postPermissions = new FullBottomSheetDialogFragment();
//                postPermissions.show(activity.getSupportFragmentManager(), "PostPermission");
                if (NetworkHelper.hasNetworkAccess(mContext)) {

                    Call<CommentItem> call = commentService.getAllPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", item.getPostId(), userIds);
                    sendAllCommentItemRequest(call);

                    //    log("Running code");
                    delayLoadComment(mProgressBar);
                } else {
                    Tools.showNetworkDialog(activity.getSupportFragmentManager());


                }

            }
        });

        layoutFollowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String followUserId=item.getPostUserid();
                setFollow(followUserId, position);
            }
        });
        unFollowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFooter postFooter=item.getPostFooter();
                postFooter.setFollowed(true);
          //      App.getAppContext().sendBroadcast(new Intent(AppConstants.FOLLOW_STATUS_BROADCAST).putExtra("post_item", (Parcelable) item).putExtra("position", position).putExtra("type", "follow"));
           //     followToggle(rootView,contentFollow,false);
                contentFollow.setVisibility(View.GONE);
            }
        });

    }

    private void sendPostUnLikeRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String status = object.getString("status");
                            if ("true".equalsIgnoreCase(status)) {

                                postLikeNumeric = Integer.parseInt(postLike);
                                postLikeNumeric = postLikeNumeric <= 0 ? 0 : --postLikeNumeric;
                                postLike = String.valueOf(postLikeNumeric);
                                item.getPostFooter().setPostTotalLike(postLike);
                                item.getPostFooter().setLikeUserStatus(false);

                                if (0 == postLikeNumeric) {
                                    tvPostLikeCount.setVisibility(View.GONE);
                                } else {
                                    SpannableString content = new SpannableString(String.valueOf(postLikeNumeric));
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    tvPostLikeCount.setVisibility(View.VISIBLE);
                                    tvPostLikeCount.setText(content);
                                }
                                imgLike.setImageResource(R.drawable.like_normal);
                            } else {
                                Call<String> mCall = webService.postLike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                                sendPostLikeRequest(mCall);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void sendPostLikeRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String status = object.getString("status");
                            if ("true".equalsIgnoreCase(status)) {
                                if (Tools.checkNormalModeIsOn(mContext))
                                    player.start();
                                Call<String> mCall = webService.sendBrowserNotification(
                                        deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                                        profileId,//"26444",
                                        token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                                        item.getPostUserid(),//"26444",
                                        userIds,//"26444",
                                        item.getPostId(),
                                        "like_post"
                                );
                                sendBrowserNotificationRequest(mCall);

                                postLikeNumeric = Integer.parseInt(postLike);
                                postLikeNumeric++;
                                postLike = String.valueOf(postLikeNumeric);
                                item.getPostFooter().setPostTotalLike(postLike);
                                item.getPostFooter().setLikeUserStatus(true);

                                SpannableString content = new SpannableString(String.valueOf(postLikeNumeric));
                                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                tvPostLikeCount.setVisibility(View.VISIBLE);
                                tvPostLikeCount.setText(content);
                                imgLike.setImageResource(R.drawable.like_done);
                            } else {

                                Call<String> mCall = webService.postUnlike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                                sendPostUnLikeRequest(mCall);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void sendBrowserNotificationRequest(Call<String> mCall) {

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void sendPostPermissionRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                if ("1".equalsIgnoreCase(isShared)) {
                                    switch (postPermissions) {
                                        case "Public":
                                            imageSharePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
                                            break;
                                        case "Friends":
                                            imageSharePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
                                            break;
                                        case "Only Me":
                                            imageSharePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
                                            break;

                                    }
                                } else {
                                    switch (postPermissions) {
                                        case "Public":
                                            imagePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
                                            break;
                                        case "Friends":
                                            imagePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
                                            break;
                                        case "Only Me":
                                            imagePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
                                            break;

                                    }
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void sendReportReason(Call<ReportReason> call) {
        call.enqueue(new Callback<ReportReason>() {

            @Override
            public void onResponse(Call<ReportReason> mCall, Response<ReportReason> response) {


                if (response.body() != null) {
                    ReportReason reportReason = response.body();
                    boolean isFollowed = reportReason.isFollowed();
                    App.setIsFollow(isFollowed);
                    List<Reason> reasonList = reportReason.getReason();
                    PostItem item = new PostItem();
                    CommentItem commentItems = new CommentItem();
                    ReportReasonSheet reportReasonSheet = ReportReasonSheet.newInstance(reasonList);
                    reportReasonSheet.show(activity.getSupportFragmentManager(), "ReportReasonSheet");

                }


            }

            @Override
            public void onFailure(Call<ReportReason> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }

    private void sendAllCommentItemRequest(Call<CommentItem> call) {

        call.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {


                if (response.body() != null) {
                    CommentItem commentItem = response.body();
                    Intent intent = new Intent(mContext, CommentPost.class);
                    intent.putExtra(COMMENT_KEY, (Parcelable) commentItem);
                    intent.putExtra(ITEM_KEY, (Parcelable) item);
                    intent.putExtra(POST_ITEM_POSITION, position);


                    mContext.startActivity(intent);


                }


            }

            @Override
            public void onFailure(Call<CommentItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

                ((Home) Objects.requireNonNull(mContext)).loadCompleteListener.onLoadComplete(1);
            }
        });

    }

    private void sendShareItemRequest(Call<PostItem> call) {


        call.enqueue(new Callback<PostItem>() {

            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {

                PostItem postShareItem = response.body();
                Log.d("Data", postShareItem.toString());
                if (postShareItem != null) {
                    //   adapter = new PostAdapter(getActivity(), postItemList);
                    //  Toast.makeText(mContext, "item selected " + item.getItemName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PostShare.class);
                    //intent.putExtra(ITEM_ID_KEY,item.getItemId());
                    intent.putExtra(ITEM_KEY, (Parcelable) postShareItem);
                    mContext.startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mDrawable = new BitmapDrawable(App.getInstance().getResources(), bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    private void setFollow(String followUserId, int position) {
//        progressBarLoading.setVisibility(View.VISIBLE);
      //  showProgressBar(mContext.getString(R.string.loading));
        Call<String> call = webService.setFollow(deviceId, token, profileId, userIds, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                      //  followToggle(rootView,contentFollow,false);
                        contentFollow.setVisibility(View.GONE);
                        PostFooter postFooter=item.getPostFooter();
                        postFooter.setFollowed(true);
                       // App.getAppContext().sendBroadcast(new Intent(AppConstants.FOLLOW_STATUS_BROADCAST).putExtra("post_item", (Parcelable) item).putExtra("position", position).putExtra("type", "follow"));
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
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
    private void showProgressBar(String title) {
        progressDialog.setMessage(title);
        progressDialog.show();
    }


    private void hideProgressBar() {
        progressDialog.dismiss();
    }
    private void sendBrowserNotification(String followUserId) {
        Call<String> call = webService.sendBrowserNotification(deviceId, userIds, token, followUserId, userIds, "0", "follow");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}