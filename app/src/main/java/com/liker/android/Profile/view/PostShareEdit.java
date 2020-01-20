package com.liker.android.Profile.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.liker.android.App;
import com.liker.android.Home.adapter.PostAdapter;
import com.liker.android.Home.holder.ImageHolder;
import com.liker.android.Home.holder.LinkScriptHolder;
import com.liker.android.Home.holder.LinkScriptYoutubeHolder;
import com.liker.android.Home.holder.TextHolder;
import com.liker.android.Home.holder.TextMimHolder;
import com.liker.android.Home.holder.VideoHolder;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.model.PostFile;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Home.model.postshare.PostShareItem;
import com.liker.android.Home.model.postshare.PostShares;
import com.liker.android.Home.model.postshare.PostTextIndex;
import com.liker.android.Home.service.HomeService;
import com.liker.android.Home.service.SocketIOManager;
import com.liker.android.Home.view.activity.EditPost;
import com.liker.android.Post.adapter.ChatAdapter;
import com.liker.android.Post.adapter.MentionUserAdapter;
import com.liker.android.Post.model.MentionUser;
import com.liker.android.Post.model.Mim;
import com.liker.android.Post.service.DataProvider;
import com.liker.android.Post.service.PostService;
import com.liker.android.Post.view.fragment.PostPermission;
import com.liker.android.Profile.service.ProfileService;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.Operation;
import com.liker.android.Tool.PageTransformer;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.SingleLineTextView;
import com.liker.android.Tool.Tools;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.App.getProxy;
import static com.liker.android.Tool.Tools.containsIllegalCharacters;
import static com.liker.android.Tool.Tools.extractMentionText;
import static com.liker.android.Tool.Tools.extractMentionUser;
import static com.liker.android.Tool.Tools.extractUrls;
import static com.liker.android.Tool.Tools.getDomainName;
import static com.liker.android.Tool.Tools.getSpannableStringBuilder;
import static com.liker.android.Tool.Tools.isNullOrEmpty;
import static java.lang.Integer.parseInt;

public class PostShareEdit extends AppCompatActivity implements
        View.OnClickListener,
        PostPermission.BottomSheetListener,
        TextHolder.PostItemListener,
        TextMimHolder.PostItemListener,
        VideoHolder.PostItemListener,
        LinkScriptYoutubeHolder.PostItemListener,
        LinkScriptHolder.PostItemListener,
        ImageHolder.PostItemListener
{

    private final String TAG = "PostShares";
    private TextView tvAudience;
    private CardView onlyImage, onlyVideo, onlyLinkScript, onlyLinkScriptYoutube;
    private LinearLayout onlyText, shareContent, onlyMim;
    ArrayList<String> mList;
    //postContent
    private ReadMoreTextView tvPostContent, tvPostContentMim, tvPostContentImage, tvPostContentLink, tvPostContentLinkYoutube, tvPostContentVideo;
    private String full_text;
    public EmojiTextView tvPostEmojiContent, tvPostEmojiContentMim, tvPostEmojiContentImage, tvPostEmojiContentLink, tvPostEmojiContentLinkYoutube, tvPostEmojiContentVideo;
    List<Mim> viewColors = new DataProvider().getMimList();
    private Drawable mDrawable;

    private ImageView imageMedia, imgLinkScript, imgLinkScriptYoutube;
    private TextView tvPostLinkTitle, tvPostLinkTitleYoutube, tvPostLinkDescription, tvPostLinkDescriptionYoutube, tvPostLinkHost, tvPostLinkHostYoutube;
    private JZVideoPlayerStandard jzVideoPlayerStandard;

    private TextView tvPermission;
    private ImageView imgPermission, imageEmoji;
    private int postPermission;
    private EmojiPopup emojiPopup;
    private ViewGroup rootView;
    EmojiEditText editPostMessage;
    private TextView tvSubmitPost;

    //Emoji
    ChatAdapter chatAdapter;

    //Network
    private boolean networkOk;
    private CircularProgressView progressView;
    public HomeService webService;
    private ProfileService profileWebService;
    private PostService postService;
    private String profileId;
    private String deviceId;
    private String userIds;
    private String toUserId;
    private String token;
    private PrefManager manager;
    private String postId;
    private String postContent;
    private String categoryId;
    private String subCategoryId;
    private String friends;
    private String postType;
    private String userName;
    private String fromUserId;

    //socket.io
    private Socket socket;
    private Headers headers;
    private PostShares postShares;
    private String imageUrl;
    private CircleImageView imgPostUser;

    //Add Header
    public CircleImageView imagePostUser;
    public ImageView imagePostPermission, imagePermission, star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    private SingleLineTextView tvPostUserName, tvPostTime;
    private TextView tvHeaderInfo;
    private RecyclerView sharedRecyclerview;
    PostAdapter adapter;
    public List<PostItem> postItemList;

    //EditPost
    PostItem item;
    String editPostId;
    private int position;
    private String contentTitle;
    private String userQuery;
    List<String> nameList = new ArrayList<>();
    private boolean rvMentionUserShow;
    //MentionUserSearch
    private String replaceContent;
    RecyclerView recyclerViewSearchMention;
    private boolean isFirstTimeShowMention;
    List<String> idList = new ArrayList<>();
    List<String> friendSet = new ArrayList<>();
    Map<String, String> wordsToReplace = new HashMap<String, String>();
    Set<String> keys = wordsToReplace.keySet();
    private String mentionMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_share);
         item = getIntent().getExtras().getParcelable(TextHolder.ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }
        position = getIntent().getIntExtra("position", -1);
        socket = SocketIOManager.wSocket;
        headers = new Headers();
        sharedRecyclerview = findViewById(R.id.sharedRecyclerview);
        postShares = new PostShares();
        networkOk = NetworkHelper.hasNetworkAccess(this);
        chatAdapter = new ChatAdapter();
        manager = new PrefManager(this);
        webService = HomeService.mRetrofit.create(HomeService.class);
        profileWebService = ProfileService.mRetrofit.create(ProfileService.class);
        postService = PostService.mRetrofit.create(PostService.class);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        rootView = findViewById(R.id.main_activity_root_view);
        tvPermission = findViewById(R.id.tvPermission);
        imgPermission = findViewById(R.id.imgPermission);
        editPostMessage = findViewById(R.id.editPostMessage);
        tvSubmitPost = findViewById(R.id.tvSubmitPost);
        tvSubmitPost.setOnClickListener(this);
        postItemList = new ArrayList<>();

        imgPostUser = findViewById(R.id.imgPostUser);
        imageUrl = manager.getProfileImage();
        if (!isNullOrEmpty(imageUrl)) {
            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile)
                    .into(imgPostUser);
            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile)
                    .into(imgPostUser);
        }

        postItemList.add(item);
        adapter = new PostAdapter(this, postItemList, this, this, this, this, this, this, "");
        sharedRecyclerview.setAdapter(adapter);

        //HEADER
        profileId = manager.getProfileId();
        userIds = manager.getProfileId();
        fromUserId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();

        //Form Data
        postId = item.getSharedPostId();
        categoryId = item.getCatId();
        subCategoryId = "";
        friends = "";
        postType = item.getPostType();
        userName = item.getPostUsername();
        toUserId = item.getPostUserid();

        onlyImage = findViewById(R.id.onlyImage);
        onlyVideo = findViewById(R.id.onlyVideo);
        onlyLinkScript = findViewById(R.id.onlyLinkScript);
        onlyLinkScriptYoutube = findViewById(R.id.onlyLinkScriptYoutube);
        onlyMim = findViewById(R.id.onlyMim);
        onlyText = findViewById(R.id.onlyText);
        recyclerViewSearchMention = (RecyclerView) findViewById(R.id.rvSearchMention);
        imageMedia = findViewById(R.id.imageMedia);
        imgLinkScript = findViewById(R.id.imgLinkScript);
        imgLinkScriptYoutube = findViewById(R.id.imgLinkScriptYoutube);
        tvPostLinkTitle = findViewById(R.id.tvPostLinkTitle);
        tvPostLinkTitleYoutube = findViewById(R.id.tvPostLinkTitleYoutube);
        tvPostLinkDescription = findViewById(R.id.tvPostLinkDescription);
        tvPostLinkDescriptionYoutube = findViewById(R.id.tvPostLinkDescriptionYoutube);
        tvPostLinkHost = findViewById(R.id.tvPostLinkHost);
        tvPostLinkHostYoutube = findViewById(R.id.tvPostLinkHostYoutube);
        jzVideoPlayerStandard = findViewById(R.id.videoplayer);

        shareContent = findViewById(R.id.shareContent);

        tvPostContent = (ReadMoreTextView) findViewById(R.id.tvPostContent);
        tvPostContentMim = (ReadMoreTextView) findViewById(R.id.tvPostContentMim);
        tvPostContentImage = (ReadMoreTextView) findViewById(R.id.tvPostContentImage);
        tvPostContentLink = (ReadMoreTextView) findViewById(R.id.tvPostContentLink);
        tvPostContentLinkYoutube = (ReadMoreTextView) findViewById(R.id.tvPostContentLinkYoutube);
        tvPostContentVideo = (ReadMoreTextView) findViewById(R.id.tvPostContentVideo);

        tvPostEmojiContent = findViewById(R.id.tvPostEmojiContent);
        tvPostEmojiContentMim = findViewById(R.id.tvPostEmojiContentMim);
        tvPostEmojiContentImage = findViewById(R.id.tvPostEmojiContentImage);
        tvPostEmojiContentLink = findViewById(R.id.tvPostEmojiContentLink);
        tvPostEmojiContentLinkYoutube = findViewById(R.id.tvPostEmojiContentLinkYoutube);
        tvPostEmojiContentVideo = findViewById(R.id.tvPostEmojiContentVideo);

        imageEmoji = findViewById(R.id.main_activity_emoji);
        imageEmoji.setOnClickListener(this);
        findViewById(R.id.contentPostPermission).setOnClickListener(this);
        findViewById(R.id.imageCancelPost).setOnClickListener(this);
        setUpEmojiPopup();

        mList = new ArrayList<>();
        tvAudience = findViewById(R.id.tvAudience);
        tvAudience.setText(item.getCatName());
        String postType = item.getPostType();
        int viewType = Integer.parseInt(postType);
        String hasMim = item.getHasMeme();
        int mimId = Integer.parseInt(hasMim);

        editPostMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                /// if(uploadImageName.)
                contentTitle = s.toString().trim();

                for (String st : contentTitle.split(" ")) {
                    if (st.startsWith("@")) {
                        userQuery = st;
                    } else {
                        userQuery = "";
                    }
                }

                if (!isNullOrEmpty(userQuery) && userQuery.length() > 1) {
                    rvMentionUserShow = true;
                    mentionUserToggle();
                    mentionUsers();
                } else if (isFirstTimeShowMention && !isNullOrEmpty(userQuery)) {
                    rvMentionUserShow = true;
                    mentionUserToggle();
                    mentionUsers();
                } else if (isFirstTimeShowMention && isNullOrEmpty(userQuery)) {
                    rvMentionUserShow = false;
                    mentionUserToggle();

                }



             if (!isNullOrEmpty(contentTitle) && contentTitle.length() > 0) {
                    tvSubmitPost.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        // TODO: 1/19/2020 update share post
        String postMessage = item.getSharedPostText();
        editPostMessage.append(postMessage);
        tvAudience.setText(item.getCatName());
        String permission = item.getPermission();
        switch (permission) {
            case "0":
                manager.setPostPermission("Public");
                postPermission = 0;
                break;
            case "1":
                manager.setPostPermission("Only me");
                postPermission = 1;
                break;
            case "2":
                manager.setPostPermission("Friends Only");
                postPermission = 2;
                break;
        }

        String permissionData = manager.getPostPermission();
        tvPermission.setText(permissionData);
        setPermissionData(permissionData);
        //   tvPermission.setText(editPostItem.getPermission());
        categoryId = item.getCatId();
        subCategoryId = item.getCatId();
        editPostId = item.getPostId();
        postPermission = Integer.parseInt(item.getPermission());


    }
    private void mentionUsers() {
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();

            Call<List<MentionUser>> call = postService.searchMentionUser(deviceId, profileId, token, userQuery);
            sendMentionUserRequest(call);


        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();
        }
    }

    private void sendMentionUserRequest(Call<List<MentionUser>> call) {
        call.enqueue(new Callback<List<MentionUser>>() {


            @Override
            public void onResponse(Call<List<MentionUser>> call, Response<List<MentionUser>> response) {


                List<MentionUser> mentionUsers = response.body();
                replaceContent = contentTitle.replace(userQuery, " ");


                MentionUserAdapter.RecyclerViewClickListener listener = (view, position) -> {

                    String name = mentionUsers.get(position).getDisplay();
                    String id = mentionUsers.get(position).getId();
                    nameList.add(name);

                    idList.add(id);
                    StringBuilder nameBuilder = new StringBuilder();
                    nameBuilder.append(name);

                    friendSet.add(id);
                    String separator = ", ";
                    int total = friendSet.size() * separator.length();
                    for (String s : friendSet) {
                        total += s.length();
                    }
                    StringBuilder sb = new StringBuilder(total);
                    for (String s : friendSet) {
                        sb.append(separator).append(s);
                    }
                    friends = sb.substring(separator.length()).replaceAll("\\s+", "");
                    if (nameList.size() > 0) {
                        //Create new list
                        String nameStr = nameBuilder.toString();
                        String[] nameArr = nameStr.split(" ");

                        StringBuilder mentionBuilder = new StringBuilder();
                        String mention_text = contentTitle.replaceAll(userQuery, "mention_" + id);

                        String full_text = contentTitle.replaceAll(userQuery, name);
                        String[] splittedWords = full_text.split(" ");
                        SpannableString str = new SpannableString(full_text);
                        //Check the matching words
                        for (int i = 0; i < nameArr.length; i++) {
                            for (int j = 0; j < splittedWords.length; j++) {
                                if (nameArr[i].equalsIgnoreCase(splittedWords[j])) {
                                    mList.add(nameArr[i]);
                                }
                            }
                        }
                        //make the words bold
                        for (int k = 0; k < mList.size(); k++) {
                            int val = full_text.indexOf(mList.get(k));
                            if (val >= 0) {
                                str.setSpan(new StyleSpan(Typeface.ITALIC), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                str.setSpan(new BackgroundColorSpan(Color.parseColor("#D8DFEA")), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        //hi azhar @[Mimi Owensby](id:3232) note @[Mijanur Rahaman](id:32) and @[Samuel Rivero](id:36)

                        for (int i = 0; i < nameList.size(); i++) {
                            String mName = nameList.get(i);
                            for (int j = 0; j < idList.size(); j++) {
                                String mId = idList.get(j);

                                if (i == j) {
                                    wordsToReplace.put(mName, "@[" + mName + "]" + "(id:" + mId + ")");
                                    //    wordsToReplace.put(mName, "mention_"+mId);//@[Mijanur Rahaman](id:32)
                                    keys = wordsToReplace.keySet();
                                    break;
                                }

                            }
                        }

                        mentionMessage = str.toString();
                        for (String key : keys) {
                            mentionMessage = mentionMessage.replace(key, wordsToReplace.get(key));
                            Log.d("message", mentionMessage);
                        }

                        Log.d("message", mentionMessage);

                        editPostMessage.setText("");
                        editPostMessage.append(str);

                    }


                    userQuery = "";
                    rvMentionUserShow = false;
                    mentionUserToggle();


                };
                MentionUserAdapter mentionUserAdapter = new MentionUserAdapter(PostShareEdit.this, mentionUsers, listener);
                recyclerViewSearchMention.setAdapter(mentionUserAdapter);


                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                isFirstTimeShowMention = true;

            }

            @Override
            public void onFailure(Call<List<MentionUser>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });

    }

    private void setPermissionData(String permissionData) {
        switch (permissionData) {
            case "Public":
                imgPermission.setImageResource(R.drawable.ic_public_black_12dp);
                postPermission = 0;
                break;
            case "Only me":
                imgPermission.setImageResource(R.drawable.ic_lock_outline_black_12dp);
                postPermission = 1;
                break;
            case "Friends Only":
                imgPermission.setImageResource(R.drawable.ic_people_outline_black_12dp);
                postPermission = 2;
                break;
        }
    }
    private void mentionUserToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(recyclerViewSearchMention);

        TransitionManager.beginDelayedTransition(rootView, transition);
        recyclerViewSearchMention.setVisibility(rvMentionUserShow ? View.VISIBLE : View.GONE);
    }
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.contentPostPermission:
                String message = tvPermission.getText().toString();
                PostPermission postPermissions = PostPermission.newInstance(message);
                postPermissions.show(getSupportFragmentManager(), "PostPermission");
                break;
            case R.id.main_activity_emoji:
                emojiPopup.toggle();
                break;
            case R.id.imageCancelPost:
                finish();
                break;
            case R.id.tvSubmitPost:

                final RecyclerView recyclerViews = findViewById(R.id.main_activity_recycler_view);
                recyclerViews.setAdapter(chatAdapter);
                recyclerViews.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                shareAsPost();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void shareAsPost() {

        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
                Call<String> call = profileWebService.editSharedPost(
                        deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                        profileId,//"26444",
                        token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                        editPostId,//"26444",
                        Integer.parseInt(profileId),//"26444",
                        contentTitle,//"status",
                        postPermission,//0,
                        categoryId,//3,
                        subCategoryId,// 54,
                        1,//1,
                        userName,//"june8045",
                        friends
                );
                sendEditSharePostRequest(call);

        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }
    private void sendEditSharePostRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*{"errors":[],"success":{"status":true},"notification_ids":[]}*/
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            JSONObject successObject = object.getJSONObject("success");
                            if (successObject.length() > 0) {
                                boolean status = successObject.getBoolean("status");
                                if (status) {
                                    startActivity(new Intent(PostShareEdit.this, ProfileActivity.class).putExtra("user_id", manager.getProfileId()).putExtra("user_name", userName));
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });
    }
    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(ignore -> Log.d(TAG, "Clicked on Backspace"))
                .setOnEmojiClickListener((ignore, ignore2) -> Log.d(TAG, "Clicked on emoji"))
                .setOnEmojiPopupShownListener(() -> imageEmoji.setImageResource(R.drawable.ic_keyboard))
                .setOnSoftKeyboardOpenListener(ignore -> Log.d(TAG, "Opened soft keyboard"))
                .setOnEmojiPopupDismissListener(() -> imageEmoji.setImageResource(R.drawable.emoji_twitter_category_smileysandpeople))
                .setOnSoftKeyboardCloseListener(() -> Log.d(TAG, "Closed soft keyboard"))
                .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
                .setPageTransformer(new PageTransformer())
                .build(editPostMessage);
    }

    @Override
    public void onButtonClicked(int image, String text) {

        tvPermission.setText(text);
        imgPermission.setImageResource(image);
        switch (text) {
            case "Public":
                postPermission = 0;
                break;
            case "Only me":
                postPermission = 1;
                break;
            case "Friends Only":
                postPermission = 2;
                break;
        }
    }

    @Override
    public void deletePost(PostItem postItem, int position) {

    }
}
