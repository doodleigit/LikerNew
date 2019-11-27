package com.liker.android.Post.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

//import com.doodle.App;
//import com.doodle.Comment.view.fragment.ReportReasonSheet;
//import com.doodle.Home.holder.mediaHolder.ImageViewHolder;
//import com.doodle.Home.holder.mediaHolder.VideoViewHolder;
//import com.doodle.Home.view.activity.Home;
//import com.doodle.Post.adapter.ChatAdapter;
//import com.doodle.Post.adapter.ImageAdapter;
//import com.doodle.Post.adapter.LinkScriptAdapter;
//import com.doodle.Post.adapter.MediaAdapter;
//import com.doodle.Post.adapter.MentionUserAdapter;
//import com.doodle.Post.adapter.MimAdapter;
//import com.doodle.Post.model.Category;
//import com.doodle.Post.model.CategoryItem;
//import com.doodle.Post.model.LinkScriptItem;
//import com.doodle.Post.model.MentionUser;
//import com.doodle.Post.model.Mim;
//import com.doodle.Post.model.MultipleMediaFile;
//import com.doodle.Post.model.PostImage;
//import com.doodle.Post.model.PostVideo;
//import com.doodle.Post.model.Subcatg;
//import com.doodle.Post.service.DataProvider;
//import com.doodle.Post.service.PostService;
//import com.doodle.Post.view.fragment.AttachmentBottomSheet;
//import com.doodle.Post.view.fragment.Audience;
//import com.doodle.Post.view.fragment.ContributorStatus;
//import com.doodle.Post.view.fragment.PostPermission;
//import com.doodle.R;
//import com.doodle.Tool.AppConstants;
//import com.doodle.Tool.EditTextLinesLimiter;
//import com.doodle.Tool.MaxLinesInputFilter;
//import com.doodle.Tool.NetworkHelper;
//import com.doodle.Tool.PageTransformer;
//import com.doodle.Tool.PrefManager;
//import com.doodle.Tool.Tools;
import com.crashlytics.android.Crashlytics;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.liker.android.App;
import com.liker.android.Home.holder.mediaHolder.ImageViewHolder;
import com.liker.android.Home.holder.mediaHolder.VideoViewHolder;
import com.liker.android.Home.model.Headers;
import com.liker.android.Home.model.PostItem;
import com.liker.android.Home.model.TopContributorStatus;
import com.liker.android.Home.service.SocketIOManager;
import com.liker.android.Post.adapter.CategoryListAdapter;
import com.liker.android.Post.adapter.ChatAdapter;
import com.liker.android.Post.adapter.ImageAdapter;
import com.liker.android.Post.adapter.LinkScriptAdapter;
import com.liker.android.Post.adapter.MediaAdapter;
import com.liker.android.Post.adapter.MentionUserAdapter;
import com.liker.android.Post.adapter.MimAdapter;
import com.liker.android.Post.model.Category;
import com.liker.android.Post.model.CategoryItem;
import com.liker.android.Post.model.ImageItem;
import com.liker.android.Post.model.LinkScrapItem;
import com.liker.android.Post.model.LinkScriptItem;
import com.liker.android.Post.model.MentionUser;
import com.liker.android.Post.model.Mim;
import com.liker.android.Post.model.MultipleMediaFile;
import com.liker.android.Post.model.PostImage;
import com.liker.android.Post.model.PostVideo;
import com.liker.android.Post.model.Subcatg;
import com.liker.android.Post.service.DataProvider;
import com.liker.android.Post.service.PostService;
import com.liker.android.Post.view.fragment.AttachmentBottomSheet;
import com.liker.android.Post.view.fragment.Audience;
import com.liker.android.Post.view.fragment.ContributorStatus;
import com.liker.android.Post.view.fragment.PostPermission;
import com.liker.android.R;
import com.liker.android.Tool.AppConstants;
import com.liker.android.Tool.EditTextLinesLimiter;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PageTransformer;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import io.socket.client.Ack;
import io.socket.client.Socket;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.liker.android.Tool.Tools.getMD5EncryptedString;
import static com.liker.android.Tool.Tools.isNullOrEmpty;
import static com.liker.android.Tool.Tools.toast;
//import static com.doodle.Tool.Tools.getMD5EncryptedString;
//import static com.doodle.Tool.Tools.isContain;
//import static com.doodle.Tool.Tools.isNullOrEmpty;
//import static com.doodle.Tool.Tools.showCustomToast;
//import static com.doodle.Tool.Tools.showKeyboard;

public class PostNew extends AppCompatActivity implements
        View.OnClickListener,
        PostPermission.BottomSheetListener,
        Audience.BottomSheetListener,
        ContributorStatus.ContributorStatusListener,
        AttachmentBottomSheet.BottomSheetListener,
        EasyPermissions.PermissionCallbacks {

    // data to populate the RecyclerView with

    private ScrollView contentPost;
    private RelativeLayout contentPostView;
    private LinearLayout contentPostPermission, messageContainer;
    private TextView tvPermission, tvAudience;
    private ImageView imgPermission;
    private PrefManager manager;
    private PostService webService, videoServices;
    private final String TAG = "PostNew";
    private boolean networkOk;
    private CircularProgressView progressView;
    boolean isGrantGallery = false;
    boolean isGrantCamera = false;


    private String profileId;
    private String deviceId;
    private String userIds;
    private String toUserId;
    private String token;
    private CategoryItem categoryItem;
    private TextView tvSubmitPost, intro;

    private MultipartBody.Part fileToUpload;

    //ExpandableListView
    //initialize

    //MIM
    RecyclerView mimRecyclerView;
    List<Mim> viewColors = new DataProvider().getMimList();
    boolean rvMimShow;
    //CATEGORY/AUDIENCE
    Drawable mDrawable;


    //Emoji
    ChatAdapter chatAdapter;
    EmojiPopup emojiPopup;
    EmojiEditText editPostMessage;
    ViewGroup rootView;
    ImageView emojiButton;

    //POST-IMAGE
    private ImageAdapter imageAdapter;
    private Uri profileImagePathUri;
    private static final int REQUEST_TAKE_CAMERA = 101;
    private static final int REQUEST_TAKE_GALLERY_IMAGE = 102;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 103;
    private static final int REQUEST_VIDEO_CAPTURE = 104;//CAMERA_REQUEST_CODE_VEDIO
    private RecyclerView mediaRecyclerView;
    boolean rvMediaShow;
    private ProgressBar mediaProgress;

    private List<PostImage> postImages = new ArrayList<>();
    private List<PostVideo> postVideos = new ArrayList<>();


    //POST LINK SCRIPT
    private EditText editText, editTextTitlePost, editTextDescriptionPost;
    private Button submitButton, randomButton;
    private FloatingActionButton postButton;
    private TextView tvOk;
    private RecyclerView rvLinkScript;
    private Context context;
    private ViewGroup dropPreview, dropPost;
    private TextView postAreaTitle;
    private String currentTitle, currentUrl, currentCannonicalUrl,
            currentDescription;

    private Bitmap[] currentImageSet;
    private Bitmap currentImage;
    private int currentItem = 0;
    private int countBigImages = 0;
    private boolean noThumb;
    private TextCrawler textCrawler;
    //    TextCrawler textCrawler = new TextCrawler();
    boolean isLinkScript;
    List<String> extractedUrls = new ArrayList<>();
    private LinearLayout linkScriptContainer;
    private boolean rvLinkScriptShow;


    //MentionUserSearch
    RecyclerView recyclerViewSearchMention;
    private boolean rvMentionUserShow;
    private String userQuery;
    private boolean isFirstTimeShowMention;

    //CREATE NEW POST
    private String postType = "status";
    private int status;
    private int postPermission;
    private String imageFile;
    private List<String> uploadImageName = new ArrayList<>();
    private int contentType;
    private String categoryId = "", subCategoryId = "";
    private String contentTitle = "";
    private String contentLinkUrl;
    private String contentHost;
    private String contentLinkTitle;
    private String contentLinkDesc;
    private String contentLinkImage;

    private String myUrl;
    private boolean isYoutubeURL;
    private int hasMim;
    private int scheduleTime = 0;
    private String friends;
    // Set<String> friendSet = new HashSet<String>();
    List<String> friendSet = new ArrayList<>();
    private String mentionSearchData;

    private View mView;
    private String categoryName, audience;
    private boolean isAddContentTitle;
    private boolean isDuplicateFile;

    private String imageFilePath, videoFilePath;
    private String fileEncoded = "";
    private String replaceContent;
    List<String> nameList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    Map<String, String> wordsToReplace = new HashMap<String, String>();
    Set<String> keys = wordsToReplace.keySet();
    private String mentionMessage;
    private String base64File;
    private String videoExtension;
    private int postId;
    private String name;

    //Multiple Media File
    List<String> mediaFiles;
    ProgressDialog progressDialog;
    private CircleImageView imgPostUser;
    private String imageUrl;


    //DELETE MEDIA
    public ImageViewHolder.ImageListener imageListener;
    public VideoViewHolder.VideoListen videoListen;

    List<String> deleteMediaFiles;
    MediaAdapter mediaAdapter;
    List<MultipleMediaFile> multipleMediaFiles;
    List<MultipleMediaFile> newMultipleMediaFiles;
    MultipleMediaFile mediaFile;
    private String mediaFive;
    private String base64md5;
    Set<String> mediaList;
    List<String> videoList;
    List<LinkScriptItem> scriptItemList;
    private Context mContext;


    private static final int READ_REQUEST_CODE = 200;
    private Uri uri;
    private String pathToStoredVideo;
    private VideoView displayRecordedVideo;
    private static final String SERVER_PATH = "";
    private String newUrl;


    private Socket socket;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_post);
        mContext = this;

        manager = new PrefManager(this);
        webService = PostService.mRetrofit.create(PostService.class);
        videoServices = PostService.videoRetrofit.create(PostService.class);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        mView = new View(this);
        mediaList = new HashSet<>();
        videoList = new ArrayList<>();
        scriptItemList = new ArrayList<>();
        mediaFile = new MultipleMediaFile();
        mediaFiles = new ArrayList<>();
        socket = SocketIOManager.nSocket;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.checking));
        multipleMediaFiles = new ArrayList<>();
        newMultipleMediaFiles = new ArrayList<>();
        mediaProgress = findViewById(R.id.mediaProgress);
        mediaRecyclerView = findViewById(R.id.rvPostMedia);
        mimRecyclerView = (RecyclerView) findViewById(R.id.rvMim);
        recyclerViewSearchMention = (RecyclerView) findViewById(R.id.rvSearchMention);
        editPostMessage = findViewById(R.id.editPostMessage);
        editPostMessage.setOnClickListener(this);

        contentPost = findViewById(R.id.contentPost);
        linkScriptContainer = findViewById(R.id.linkScriptContainer);

        findViewById(R.id.btnAttachment).setOnClickListener(this);
        intro = findViewById(R.id.intro);
        tvSubmitPost = findViewById(R.id.tvSubmitPost);
        postButton = (FloatingActionButton) findViewById(R.id.post);

        tvSubmitPost.setOnClickListener(this);
        findViewById(R.id.contentCategory).setOnClickListener(this);
        findViewById(R.id.imageCamera).setOnClickListener(this);
        findViewById(R.id.imageGallery).setOnClickListener(this);
        findViewById(R.id.imageVideo).setOnClickListener(this);
        findViewById(R.id.imageCancelPost).setOnClickListener(this);
        tvPermission = findViewById(R.id.tvPermission);
        imgPermission = findViewById(R.id.imgPermission);
        String permissionData=manager.getPostPermission();
        tvPermission.setText(permissionData);
        setPermissionData(permissionData);

        tvAudience = findViewById(R.id.tvAudience);
        tvAudience.setText(manager.getPostAudience().isEmpty() ? getString(R.string.audience) : manager.getPostAudience());
        contentPostPermission = findViewById(R.id.contentPostPermission);
        contentPostPermission.setOnClickListener(this);
        contentPostView = findViewById(R.id.contentPostView);
        messageContainer = findViewById(R.id.messageContainer);
        messageContainer.setOnClickListener(this);

        deleteMediaFiles = new ArrayList<>();
        mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
        imageListener = new ImageViewHolder.ImageListener() {
            @Override
            public void deleteImage(PostImage postImage, int position) {


                postImages.remove(postImage);
                mediaAdapter.deleteItem(position);
                mediaRecyclerView.scrollToPosition(position);
                String mdFiveFile = postImage.getMdFive();
//{"base_64_md5":"ded330ca6a57c3dbb81d292f4528d15a","file_type":"image","name":"5d861a0055159.jpg"}
                for (int i = 0; i < mediaFiles.size(); i++) {

                    try {
                        JSONObject ds = new JSONObject(mediaFiles.get(i).toString());
                        base64md5 = ds.getString("base_64_md5");
                        if (base64md5.equalsIgnoreCase(mdFiveFile)) {
                            mediaFiles.remove(i);
                        }
                        Log.d("base64md5 ", base64md5 + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        videoListen = new VideoViewHolder.VideoListen() {
            @Override
            public void deleteVideo(PostVideo postVideo, int position) {
                postVideos.remove(postVideo);
                mediaAdapter.deleteItem(position);
                mediaRecyclerView.scrollToPosition(position);
                String mdFiveFile = postVideo.getMdFive();


                for (int i = 0; i < mediaFiles.size(); i++) {

                    try {
                        JSONObject ds = new JSONObject(mediaFiles.get(i).toString());
                        base64md5 = ds.getString("base_64_md5");
                        if (base64md5.equalsIgnoreCase(mdFiveFile)) {
                            mediaFiles.remove(i);
                        }
                        Log.d("base64md5 ", base64md5 + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        MimAdapter.RecyclerViewClickListener listener = (view, position) -> {


            String mimColor = viewColors.get(position).getMimColor();
            hasMim = viewColors.get(position).getId();
            if (mimColor.startsWith("#")) {
                if (mimColor.contentEquals("#FFFFFF")) {
                    editPostMessage.addTextChangedListener(new EditTextLinesLimiter(editPostMessage, 100));
                    int mColor = Color.parseColor(mimColor);
                    messageContainer.setBackgroundColor(mColor);
                    messageContainer.setGravity(Gravity.START);
                    editPostMessage.setGravity(Gravity.START);
                    editPostMessage.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                    editPostMessage.setTextColor(Color.parseColor("#000000"));
                    ViewGroup.LayoutParams params = messageContainer.getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen._220sdp);
                    messageContainer.setLayoutParams(params);
                } else {
                    editPostMessage.addTextChangedListener(new EditTextLinesLimiter(editPostMessage, 4));
                    int mColor = Color.parseColor(mimColor);
                    messageContainer.setBackgroundColor(mColor);
                    if (mimColor.contentEquals("#C6FFD4")) {
                        editPostMessage.setTextColor(Color.parseColor("#000000"));
                    }
                    ViewGroup.LayoutParams params = messageContainer.getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen._200sdp);
                    messageContainer.setLayoutParams(params);
                    messageContainer.setGravity(Gravity.CENTER);
                    editPostMessage.setGravity(Gravity.CENTER);
                    //   editPostMessage.setFilters(new InputFilter[]{new MaxLinesInputFilter(2)});
                    editPostMessage.setTextAppearance(this, android.R.style.TextAppearance_Large);
                    editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));

                }


            } else {
                editPostMessage.addTextChangedListener(new EditTextLinesLimiter(editPostMessage, 4));
                String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                Picasso.with(this).load(imageUrl).into(target);
                messageContainer.setBackground(mDrawable);
                editPostMessage.setHeight(150);
                switch (mimColor) {
                    case "img_bg_birthday.png":
                        editPostMessage.setTextColor(Color.parseColor("#000000"));
                        break;
                    case "img_bg_love.png":
                        editPostMessage.setTextColor(Color.parseColor("#2D4F73"));
                        break;
                    case "img_bg_love2.png":
                        editPostMessage.setTextColor(Color.parseColor("#444748"));
                        break;
                    case "img_bg_red.png":
                        editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "img_bg_love3.png":
                        editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                }

            }


        };
        MimAdapter adapter = new MimAdapter(this, viewColors, listener);
        mimRecyclerView.setAdapter(adapter);

        profileId = manager.getProfileId();
        userIds = manager.getProfileId();
        toUserId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();

        chatAdapter = new ChatAdapter();


        rootView = findViewById(R.id.main_activity_root_view);
        emojiButton = findViewById(R.id.main_activity_emoji);
        final ImageView sendButton = findViewById(R.id.main_activity_send);
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


//        emojiButton.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);
//        sendButton.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);

        //   emojiButton.setOnClickListener(ignore -> emojiPopup.rvMimToggle());
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emojiPopup.toggle();


            }
        });
        sendButton.setOnClickListener(ignore -> {
            //   final String text = editPostMessage.getText().toString().trim();
            final String text = editPostMessage.getText().toString().trim();

            if (text.length() > 0) {
                chatAdapter.add(text);

                editPostMessage.setText("");
            }
        });

        final RecyclerView recyclerViews = findViewById(R.id.main_activity_recycler_view);
        recyclerViews.setAdapter(chatAdapter);
        recyclerViews.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        setUpEmojiPopup();

        textCrawler = new TextCrawler();

        editTextTitlePost = null;
        editTextDescriptionPost = null;

        /** --- From ShareVia Intent */
        if (getIntent().getExtras() != null) {
            String shareVia = (String) getIntent().getExtras().get(Intent.EXTRA_TEXT);
            if (shareVia != null) {
                editText.setText(shareVia);
            }
        }
        if (getIntent().getAction() == Intent.ACTION_VIEW) {
            Uri data = getIntent().getData();
            String scheme = data.getScheme();
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            String builded = scheme + "://" + host + "/";

            for (String string : params) {
                builded += string + "/";
            }

            if (data.getQuery() != null && !data.getQuery().equals("")) {
                builded = builded.substring(0, builded.length() - 1);
                builded += "?" + data.getQuery();
            }

            System.out.println(builded);

            editText.setText(builded);

        }
        /** --- */


        rvLinkScript = findViewById(R.id.rvLinkScript);


        postAreaTitle = (TextView) findViewById(R.id.post_area);

        /** Where the previews will be dropped */
        dropPreview = (ViewGroup) findViewById(R.id.drop_preview);

        /** Where the previews will be dropped */
        dropPost = (ViewGroup) findViewById(R.id.drop_post);


        initPostButton();


        editPostMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  makeText(PostNew.this, "beforeTextChanged " + s, LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //   makeText(PostNew.this, "onTextChanged " + s, LENGTH_SHORT).show();
                extractedUrls = Tools.extractUrls(s.toString());
                /// if(uploadImageName.)
                contentTitle = s.toString().trim();


                //  String s = "my very long string to test";

                for (String st : contentTitle.split(" ")) {
                    if (st.startsWith("@")) {
                        userQuery = st;
                    } else {
                        userQuery = "";
                    }
                }

                if (nameList.size() > 0) {
                    rvMimShow = false;
                    rvMimToggle();
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

                if (extractedUrls.size() == 0) {
                    isLinkScript = false;
                  /*  releasePreviewArea();
                    rvLinkScriptShow = false;
                    linkScriptToggle();*/
                    myUrl = "";
                    if (!isNullOrEmpty(contentTitle)) {
                        //  makeText(PostNew.this, "Button Enable-1!", LENGTH_SHORT).show();
                    }
                }
                if (extractedUrls.size() > 0) {
                    rvMimShow = false;
                    rvMimToggle();
                    rvMediaShow = false;
                    mediaRecyclerViewToggle();

                    rvLinkScriptShow = true;
                    linkScriptToggle();


                    StringBuilder builder = new StringBuilder();
                    for (String temp : extractedUrls) {
                        builder.append(temp);
                    }
                    myUrl = builder.toString();


                }
                if (!isLinkScript && extractedUrls.size() == 1) {
                    for (String url : extractedUrls) {

                        newUrl = url;
                        if (NetworkHelper.hasNetworkAccess(PostNew.this)) {
                            progressDialog.show();
                            Call<String> call = webService.isDuplicateLink(deviceId, profileId, token, userIds, "3", url);
                            sendIsDuplicateUrl(call, url);
                        } else {
                            Tools.showNetworkDialog(getSupportFragmentManager());
                            progressDialog.dismiss();

                        }

//                        try {
//                            textCrawler.makePreview(callback, url);
//                        } catch (Exception e) {}
                        isLinkScript = true;
                        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
                        if (!url.isEmpty() && url.matches(pattern)) {
                            /// Valid youtube URL
                            isYoutubeURL = true;
                        } else {
                            isYoutubeURL = false;
                            // Not Valid youtube URL
                        }
                    }

                    //  submitLink();
                }


                if (!contentTitle.isEmpty() && myUrl != null && contentTitle.length() >= myUrl.length() + 8) {
                    isAddContentTitle = true;
                    tvSubmitPost.setVisibility(View.VISIBLE);

                } else if (/*myUrl.isEmpty() */isNullOrEmpty(myUrl) && !isNullOrEmpty(contentTitle) && contentTitle.length() > 0) {
                    isAddContentTitle = true;
                    tvSubmitPost.setVisibility(View.VISIBLE);
                }/* else if (*//*myUrl.isEmpty() *//*myUrl.equalsIgnoreCase("https://w") && !isNullOrEmpty(contentTitle) && contentTitle.length() > 0) {
                    isAddContentTitle = true;
                    tvSubmitPost.setVisibility(View.VISIBLE);
                }
*/
                if (s.length() == 1) {
                    if (manager.getNewPostIntro().equals("0")) {
                        manager.setNewPostIntro("1");
                        hideKeyboard(PostNew.this);
                        showIntroTooltip();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isNullOrEmpty(mentionMessage)) {
                    //mentionMessage += s;
//                    makeText(PostNew.this, "mention-data: " + mentionMessage, LENGTH_SHORT).show();
                }

                //  List<String> extractedUrls = extractUrls("ForgotPasswords to https://stackoverflow.com/ and here is another link http://www.google.com/ \n which is a great search engine");


            }

        });

        //   progressDialog.setCancelable(false);


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
            case "Followers Only":
                imgPermission.setImageResource(R.drawable.ic_people_outline_black_12dp);
                postPermission = 2;
                break;
        }
    }

    private void sendIsDuplicateUrl(Call<String> call, String url) {

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                toast(PostNew.this, "Sorry! You have already shared this link. Please try a different link.", R.drawable.ic_warning_black_24dp);
                            } else {
                                Call<LinkScrapItem> callLink = webService.linkScrapUrl(deviceId, profileId, token, url);
                                sendLinkScrapRequest(callLink);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressDialog.dismiss();
            }
        });


    }

    private void sendLinkScrapRequest(Call<LinkScrapItem> callLink) {

        callLink.enqueue(new Callback<LinkScrapItem>() {


            @Override
            public void onResponse(Call<LinkScrapItem> call, Response<LinkScrapItem> response) {

                LinkScrapItem linkScrapItem = response.body();
                contentType = linkScrapItem.getContentType();
                if (contentType == 3) {
                    status = 3;
                } else if (contentType == 4) {
                    status = 4;
                }
                urlscrapting(linkScrapItem);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<LinkScrapItem> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showIntroTooltip() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        config.setDismissTextStyle(Typeface.DEFAULT_BOLD);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "4");

        sequence.setConfig(config);

        sequence.addSequenceItem(intro,
                getString(R.string.since_liker_is_the_smarter_social_network), getString(R.string.ok_i_got_it));

        sequence.addSequenceItem(tvAudience,
                getString(R.string.you_must_select_an_audience_for_each_post), getString(R.string.ok_i_got_it));

        sequence.start();
    }

    private void mentionUsers() {
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();

            Call<List<MentionUser>> call = webService.searchMentionUser(deviceId, profileId, token, userQuery);
            sendMentionUserRequest(call);


        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    ArrayList<String> mList = new ArrayList<>();

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

                        //   String[] nameArr = new String[nameList.size()];
                        //   nameArr = nameList.toArray(nameArr);
                        StringBuilder mentionBuilder = new StringBuilder();
                        String mention_text = contentTitle.replaceAll(userQuery, "mention_" + id);

                        String full_text = contentTitle.replaceAll(userQuery, name);
                        //  SpannableString spannableStr = new SpannableString(full_text);
                        //main string
                        //  String full_text = "We love our country a lot.";
                        //split strings by space
                        String[] splittedWords = full_text.split(" ");
                        SpannableString str = new SpannableString(full_text);
                        Log.d(TAG, "onResponse: " + splittedWords);

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

//                        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#D8DFEA"));
//                        spannableStr.setSpan(backgroundColorSpan, full_text.length()-name.length(), full_text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                        //hi azhar @[Mimi Owensby](id:3232) note @[Mijanur Rahaman](id:32) and @[Samuel Rivero](id:36)


                        //  Pattern p = Pattern.compile("cat");

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

                    rvMimShow = false;
                    rvMimToggle();

                };
                MentionUserAdapter mentionUserAdapter = new MentionUserAdapter(PostNew.this, mentionUsers, listener);
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

    private void rvMimVisibility() {
        if (mimRecyclerView.getVisibility() == View.VISIBLE) {
            rvMimShow = false;
        } else {
            rvMimShow = true;
        }
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {

            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }


    private void initPostButton() {
        postButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submitLink();

            }
        });
    }

    private void submitLink() {
        postAreaTitle.setVisibility(View.VISIBLE);
        tvSubmitPost.setVisibility(View.VISIBLE);
        postButton.setVisibility(View.GONE);
        tvSubmitPost.setEnabled(true);

        /** Inflating the preview layout */
        View mainView = getLayoutInflater().inflate(R.layout.main_view,
                null);

        LinearLayout linearLayout = (LinearLayout) mainView
                .findViewById(R.id.external);

        /**
         * Inflating the post content
         */
        final View content = getLayoutInflater().inflate(
                R.layout.post_content, linearLayout);

        /** Fullfilling the content layout */
        final LinearLayout infoWrap = (LinearLayout) content
                .findViewById(R.id.info_wrap);

        final TextView contentTextView = (TextView) content
                .findViewById(R.id.post_content);
        final ImageView imageView = (ImageView) content
                .findViewById(R.id.image_post);
        final TextView titleTextView = (TextView) content
                .findViewById(R.id.title);

        final TextView urlTextView = (TextView) content
                .findViewById(R.id.url);
        final TextView descriptionTextView = (TextView) content
                .findViewById(R.id.description);

        contentTextView.setText(TextCrawler.extendedTrim(editPostMessage
                .getText().toString()));

        if (currentImage != null && !noThumb) {
            imageView.setImageBitmap(currentImage);
        } else {

        }

        if (!currentTitle.equals(""))
            titleTextView.setText(currentTitle);
        else
            titleTextView.setVisibility(View.GONE);

        if (!currentDescription.equals(""))
            descriptionTextView.setText(currentDescription);
        else
            descriptionTextView.setVisibility(View.GONE);

        urlTextView.setText(currentCannonicalUrl);

        final String currentUrlLocal = currentUrl;


        mainView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String url = currentUrlLocal;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        dropPost.removeAllViews();
        dropPost.addView(mainView, 0);
        dropPreview.removeAllViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        tvPermission.setText(manager.getPostPermission());
        String permissionData=manager.getPostPermission();
        tvPermission.setText(permissionData);
        setPermissionData(permissionData);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        tvPermission.setText(manager.getPostPermission());
        String permissionData=manager.getPostPermission();
        tvPermission.setText(permissionData);
        setPermissionData(permissionData);
        Category mCategory = App.getmCategory();
        Subcatg mSubcatg = App.getmSubcatg();

        if (mCategory != null) {
            categoryId = mCategory.getCategoryId();
            App.setCategoryId(mCategory.getCategoryId());
            categoryName = mCategory.getCategoryName();
            audience = "";
            manager.setPostAudience(categoryName);
            tvAudience.setText(categoryName);
        }
        if (mSubcatg != null) {
            subCategoryId = mSubcatg.getSubCategoryId();
            audience = mSubcatg.getSubCategoryName();
            manager.setPostAudience(audience);
            tvAudience.setText(audience);
        }

//        if (mSubcatg != null && mCategory != null) {
//            categoryId = mCategory.getCategoryId();
//            subCategoryId = mSubcatg.getSubCategoryId();
//            App.setCategoryId(mCategory.getCategoryId());
//            categoryName = mCategory.getCategoryName();
//            audience = mSubcatg.getSubCategoryName();
//            manager.setPostAudience(audience);
//            tvAudience.setText(audience);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        tvPermission.setText(manager.getPostPermission());
        String permissionData=manager.getPostPermission();
        tvPermission.setText(permissionData);
        setPermissionData(permissionData);
        Category mCategory = App.getmCategory();
        Subcatg mSubcatg = App.getmSubcatg();

        if (mCategory != null) {
            categoryId = mCategory.getCategoryId();
            App.setCategoryId(mCategory.getCategoryId());
            categoryName = mCategory.getCategoryName();
            audience = "";
            manager.setPostAudience(categoryName);
            tvAudience.setText(categoryName);
        }
        if (mSubcatg != null) {
            subCategoryId = mSubcatg.getSubCategoryId();
            audience = mSubcatg.getSubCategoryName();
            manager.setPostAudience(audience);
            tvAudience.setText(audience);
        }

//        if (mSubcatg != null && mCategory != null) {
//            categoryId = mCategory.getCategoryId();
//            App.setCategoryId(mCategory.getCategoryId());
//            subCategoryId = mSubcatg.getSubCategoryId();
//            categoryName = mCategory.getCategoryName();
//            audience = mSubcatg.getSubCategoryName();
//            manager.setPostAudience(audience);
//            tvAudience.setText(audience);
//        }

        editPostMessage.requestFocus();

        editPostMessage.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editPostMessage, 0);
            }
        }, 200);
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
            case R.id.btnAttachment:
                break;
            case R.id.imageCancelPost:
                finish();
                break;

            case R.id.messageContainer:
//                editPostMessage.setEnabled(true);
//                if (postVideos.isEmpty() && postImages.isEmpty()) {
//                    rvMimShow = true;
//                    rvMimToggle();
//                } else {
//                    rvMimShow = false;
//                    rvMimToggle();
//                }
//                if (postVideos.isEmpty()) {
//                    rvMimShow = true;
//                    rvMimToggle();
//                } else {
//                    rvMimShow = false;
//                    rvMimToggle();
//                }
//                if (postImages.isEmpty()) {
//                    rvMimShow = true;
//                    rvMimToggle();
//                } else {
//                    rvMimShow = false;
//                    rvMimToggle();
//                }
                break;
            case R.id.editPostMessage:
                if (postVideos.isEmpty() && postImages.isEmpty()) {
                    rvMimShow = true;
                    rvMimToggle();
                } else {
                    rvMimShow = false;
                    rvMimToggle();
                }
                if (postVideos.isEmpty()) {
                    rvMimShow = true;
                    rvMimToggle();
                } else {
                    rvMimShow = false;
                    rvMimToggle();
                }
                if (postImages.isEmpty()) {
                    rvMimShow = true;
                    rvMimToggle();
                } else {
                    rvMimShow = false;
                    rvMimToggle();
                }
                break;
            case R.id.tvSubmitPost:
                if (contentType == 0) {
                    checkContentType();
                }
                if (contentTitle.isEmpty()) {
                    Tools.showCustomToast(PostNew.this, mView, "Please add a post description", Gravity.TOP);
                } else if (categoryId.isEmpty() && subCategoryId.isEmpty()) {
                    Tools.showCustomToast(PostNew.this, mView, "Please select your post’s audience.", Gravity.TOP);
                } else if (!isAddContentTitle) {
                    Tools.showCustomToast(PostNew.this, mView, "Cat’s got your tongue? Please write at least 8 characters in your post description.", Gravity.TOP);
                } else if (contentTitle.length() < 8) {
                    Tools.showCustomToast(PostNew.this, mView, "Cat’s got your tongue? Please write at least 8 characters in your post description.", Gravity.TOP);
                } else {
                    createNewPost();
                }
                // Tools.showCustomToast(LikerSearch.this, mView, " Write Minimum Three Characters !", Gravity.TOP);


//                String linkText = editPostMessage.getText().toString();
//                textCrawler.makePreview(callback, linkText);
//                if (linkText.startsWith("http")) {
//                }
                break;
            case R.id.contentCategory:

                Intent intent = new Intent(PostNew.this, PostCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                break;
            case R.id.imageCamera:
                List<String> reasonList = new ArrayList<>();
                reasonList.add("azhar");
                AttachmentBottomSheet attachmentBottomSheet = AttachmentBottomSheet.newInstance(reasonList);
                attachmentBottomSheet.show(getSupportFragmentManager(), "AttachmentBottomSheet");
//                rvMimShow = false;
//                rvMimToggle();
//                if (isGrantCamera) {
//                    sendImageFromCamera();
//                } else {
//                    checkCameraPermission();
//                }

                break;
            case R.id.imageGallery:

                rvMimShow = false;
                rvMimToggle();
                if (isGrantGallery) {

                    sendImageFromGallery();
                } else {
                    checkGalleryPermission();
                }
                //
                break;
            case R.id.imageVideo:
                rvMimShow = false;
                rvMimToggle();
                checkVideoPermission();

                break;
        }
    }

    private void createNewPost() {
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<String> call = webService.postAdded(
                    deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                    profileId,//"26444",
                    token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                    Integer.parseInt(userIds),//"26444",
                    toUserId,//"26444",
                    postType,//"status",
                    status,//0,
                    imageFile,
                    fileEncoded,//"",
                    postPermission,//0,
                    categoryId,//3,
                    subCategoryId,// 54,
                    contentType,//1,
                    contentTitle,//contentTitle,//"Here is studio.. ",
                    contentLinkUrl,//"",
                    contentHost,//"",
                    contentLinkTitle,//"",
                    contentLinkDesc,//"",
                    contentLinkImage,//"",
                    "abcdxyz",
                    profileId,//"june8045",
                    friends,//"",
                    scheduleTime,//0,
                    hasMim,//0
                    mediaFiles.toString()
            );
            sendNewPostRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    private void checkContentType() {
        if (contentTitle != null)
          /*  if (contentTitle.length() > 0 && extractedUrls.size() < 0 && isNullOrEmpty(imageFile) && postVideos.size() < 0 && postImages.size() < 0) {
                contentType = 1;
                status = 1;
            } else*/
            if (contentTitle.length() > 0 && postImages.size() > 0) {
                contentType = 2;
                status = 2;


            }/* else if (contentTitle.length() > 0 && extractedUrls.size() > 0) {
                contentType = 3;
                status = 3;
            } else if (contentTitle.length() > 0 && isYoutubeURL) {
                contentType = 4;
                status = 4;
            } */ else if (contentTitle.length() > 0 && postVideos.size() > 0) {
                contentType = 2;
                status = 2;

            } else {
                contentType = 1;
                status = 1;

                if (!isNullOrEmpty(mentionMessage)) {
                    for (String temp : nameList) {
                        name = temp;
                    }
                    if (contentTitle.contains(name)) {
                        String text = contentTitle;
                        String lastPlainText = text.substring((text.indexOf(name) + name.length()));
                        mentionMessage += lastPlainText;
                        contentTitle = mentionMessage;
                    }

                }
            }
    }

    private void rvMimToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(mimRecyclerView);

        TransitionManager.beginDelayedTransition(rootView, transition);
        mimRecyclerView.setVisibility(rvMimShow ? View.VISIBLE : View.GONE);
    }

    private void mediaRecyclerViewToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(mediaRecyclerView);

        TransitionManager.beginDelayedTransition(rootView, transition);
        mediaRecyclerView.setVisibility(rvMediaShow ? View.VISIBLE : View.GONE);
    }

    private void linkScriptToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(linkScriptContainer);

        TransitionManager.beginDelayedTransition(rootView, transition);
        linkScriptContainer.setVisibility(rvLinkScriptShow ? View.VISIBLE : View.GONE);
    }

    private void mentionUserToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(recyclerViewSearchMention);

        TransitionManager.beginDelayedTransition(rootView, transition);
        recyclerViewSearchMention.setVisibility(rvMentionUserShow ? View.VISIBLE : View.GONE);
    }

    private void sendNewPostRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        try {
                            JSONObject object = new JSONObject(response.body());
                            JSONObject successObject = object.getJSONObject("success");
                            if (successObject.length() > 0) {
                                boolean topContributorStatus = successObject.getBoolean("top_contributor_status");
                                postId = successObject.getInt("post_id");

                                if (topContributorStatus) {
                                    String msg;
                                    if (audience.isEmpty()) {
                                        msg = categoryName + "-" + audience + " ?";
                                    } else {
                                        msg = categoryName + "-" + audience + " ?";
                                    }
//                                ResendEmail resendEmail = ResendEmail.newInstance(msg);
//                                resendEmail.show(getSupportFragmentManager(), "ResendEmail");
                                    ContributorStatus status = ContributorStatus.newInstance(msg);
                                    status.show(getSupportFragmentManager(), "ContributorStatus");
                                } else {
                             /*   if (contentType == 5) {

                                    Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                                    sendVideoRequest(mediaCall);

                                } else {

                                }*/
//                                    startActivity(new Intent(PostNew.this, Home.class));
                                    finish();
                                    sendBroadcast((new Intent()).setAction(AppConstants.NEW_POST_ADD_BROADCAST));
                                }
                                 TopContributorStatus contributorStatus=new TopContributorStatus();
                                Headers headers=new Headers();
                                 //   String categoryId = App.getCategoryId();
                                    headers.setDeviceId(deviceId);
                                    headers.setIsApps(true);
                                    headers.setSecurityToken(token);
                                    headers.setUserId(userIds);
                                    contributorStatus.setCategoryId(categoryId);
                                    contributorStatus.setUserId(userIds);
                                    contributorStatus.setMaxPostId("423487");
                                    contributorStatus.setPermission(String.valueOf(postPermission));
                                    contributorStatus.setHeaders(headers);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(contributorStatus);

                                    socket.emit("new_post", json, new Ack() {
                                        @Override
                                        public void call(Object... args) {

                                        }
                                    });


                            }

                            JSONObject errorObject = object.getJSONObject("errors");
                            if (errorObject.length() > 0) {
                                String post_duplicate = errorObject.getString("post_duplicate");
                                Tools.toast(PostNew.this, post_duplicate, R.drawable.ic_warning_black_24dp);
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
            case "Followers Only":
                postPermission = 2;
                break;


        }
    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            mDrawable = new BitmapDrawable(getResources(), bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    @Override
    public void onDestroy() {  // could be in onPause or onStop
        Picasso.with(this).cancelRequest(target);
        App.setmCategory(null);
        App.setmSubcatg(null);
        manager.setPostAudience("");
        super.onDestroy();
        textCrawler.cancel();
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(ignore -> Log.d(TAG, "Clicked on Backspace"))
                .setOnEmojiClickListener((ignore, ignore2) -> Log.d(TAG, "Clicked on emoji"))
                .setOnEmojiPopupShownListener(() -> emojiButton.setImageResource(R.drawable.ic_keyboard))
                .setOnSoftKeyboardOpenListener(ignore -> Log.d(TAG, "Opened soft keyboard"))
                .setOnEmojiPopupDismissListener(() -> emojiButton.setImageResource(R.drawable.emoji_twitter_category_smileysandpeople))
                .setOnSoftKeyboardCloseListener(() -> Log.d(TAG, "Closed soft keyboard"))
                .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
                .setPageTransformer(new PageTransformer())
                .build(editPostMessage);
    }

    Uri imageUri;

    public void sendImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        imageUri = getImageUri();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_CAMERA);
            //  startActivityForResult(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA), REQUEST_TAKE_CAMERA);

        }

    }

    public void sendImageFromGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_TAKE_GALLERY_IMAGE);

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent1 = new Intent();
            intent1.setType("image/*");
            intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent1, "Select images"), REQUEST_TAKE_GALLERY_IMAGE);
        } else {
            Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent2.addCategory(Intent.CATEGORY_OPENABLE);
            intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent2.setType("image/*");
            startActivityForResult(intent2, REQUEST_TAKE_GALLERY_IMAGE);
        }

    }


    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_GALLERY_IMAGE);
            isGrantGallery = false;

        } else {
            sendImageFromGallery();
            //     makeText(this, R.string.grant, LENGTH_SHORT).show();
            isGrantGallery = true;
        }
    }

    private void checkVideoPermission() {
        /*RUN TIME PERMISSIONS*/

        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(PostNew.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(PostNew.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(PostNew.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_TAKE_GALLERY_VIDEO);
            }
        } else {
            sendVideoFroGallery();
            //  makeText(this, R.string.grant, LENGTH_SHORT).show();
        }
    }

    private void sendVideoFroGallery() {

//        Intent intent = new Intent();
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select PostVideo"), REQUEST_TAKE_GALLERY_VIDEO);

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent1 = new Intent();
            intent1.setType("video/mp4");
            intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent1, "Select videos"), REQUEST_TAKE_GALLERY_VIDEO);
        } else {
            Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent2.addCategory(Intent.CATEGORY_OPENABLE);
            intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent2.setType("video/mp4");
            startActivityForResult(intent2, REQUEST_TAKE_GALLERY_VIDEO);
        }


    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_CAMERA);
            isGrantCamera = false;

        } else {
            sendImageFromCamera();
            isGrantCamera = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, PostNew.this);
        switch (requestCode) {
            case REQUEST_TAKE_GALLERY_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //   makeText(this, R.string.gallery_granted, LENGTH_SHORT).show();
                    sendImageFromGallery();
                } else {
                    makeText(this, R.string.request_permission, LENGTH_SHORT).show();
                }
                break;
            case REQUEST_TAKE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //     makeText(this, R.string.gallery_granted, LENGTH_SHORT).show();
                    sendImageFromCamera();
                } else {
                    makeText(this, R.string.request_permission, LENGTH_SHORT).show();
                }
                break;
            case REQUEST_TAKE_GALLERY_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  makeText(this, R.string.gallery_granted, LENGTH_SHORT).show();
                    sendVideoFroGallery();
                } else {
                    makeText(this, R.string.request_permission, LENGTH_SHORT).show();
                }
                break;

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    getSelectedImagesPath(requestCode, data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "Cancel Gallery", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_TAKE_CAMERA) {

            if (resultCode == RESULT_OK) {


                String imagePath = imageUri.getPath();
                File file = new File(imagePath);

                String strMD5 = getMD5EncryptedString(imagePath);
                fileEncoded = strMD5;

                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                Call<String> mediaCall = webService.addPhoto(deviceId, profileId, token, fileToUpload);
                addPhotoRequest(mediaCall, fileEncoded);

                progressDialog.show();
                postImages.add(new PostImage("file://" + imagePath, "", fileEncoded, false));
                if (postImages.size() > 0)
                    rvMediaShow = true;
                mediaRecyclerViewToggle();
                mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
                mediaRecyclerView.setAdapter(mediaAdapter);
                progressDialog.dismiss();

            } else {
                Toast.makeText(this, "Cancel Camera Capture", Toast.LENGTH_SHORT).show();
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            uri = data.getData();
            if (EasyPermissions.hasPermissions(PostNew.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // imageUri = getVideoUri();
                if (uri == null)
                    return;
                pathToStoredVideo = getRealPathFromURIPath(uri, PostNew.this);
                //imageUri = getImageUri();

                File file = new File(pathToStoredVideo);
                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
                Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                sendUploadVideoRequest(mediaCall);

                progressDialog.show();

                String videoPath = "file://" + pathToStoredVideo;
                PostVideo postVideo = new PostVideo();
                postVideo.setVideoPath(videoPath);
                postVideo.setMdFive(fileEncoded);
                postVideo.setDuplicate(false);
                postVideos.add(postVideo);
                if (postVideos.size() > 0)
                    rvMediaShow = true;
                mediaRecyclerViewToggle();
                mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
                mediaRecyclerView.setAdapter(mediaAdapter);


            } else {
                EasyPermissions.requestPermissions(PostNew.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {

                try {
                    getSelectedVideosPath(requestCode, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }


    }

    private void uploadVideoToServer(String pathToVideoFile) {
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);

        Log.d("videoPath: ", vFile + "");

     /*     Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
      VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject>  serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if(!TextUtils.isEmpty(result.getSuccess())){
                    Toast.makeText(PostNew.this, "Result " + result.getSuccess(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Result " + result.getSuccess());
                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
            }
        });*/
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String m_curentDateandTime = m_sdf.format(new Date());
            return contentURI.getPath()+m_curentDateandTime;
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String result = cursor.getString(idx);
            cursor.close();
            return result;
        }
    }

    private String getFileDestinationPath() {
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment + "/video/");
        if (!directoryFolder.exists()) {
            directoryFolder.mkdir();
        }
        Log.d(TAG, "Full path " + filePathEnvironment + "/video/" + generatedFilename + ".mp4");
        return filePathEnvironment + "/video/" + generatedFilename + ".mp4";
    }


    private void sendVideoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());

//                            startActivity(new Intent(PostNew.this, Home.class));
                            finish();
                            sendBroadcast((new Intent()).setAction(AppConstants.NEW_POST_ADD_BROADCAST));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

    private void addPhotoRequest(Call<String> call, String fileEncoded) {


        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean mediaStatus = object.getBoolean("status");
                            imageFile = object.getString("filename");
                            uploadImageName.add(imageFile);
                            //  MultipleMediaFile mediaFile = new MultipleMediaFile();
                            mediaFile.setBase64Md5(fileEncoded);
                            mediaFile.setFileType("image");
                            mediaFile.setName(imageFile);
                            multipleMediaFiles.add(mediaFile);

                            Gson gson = new Gson();
                            String gsonString = gson.toJson(mediaFile);
                            mediaFiles.add(gsonString);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

    private void uploadFile(Bitmap imageBitmap, int i) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        imageUri = getImageUri();
    }

    private Uri getImageUri() {
        Uri m_imgUri = null;
        File m_file;
        try {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String m_curentDateandTime = m_sdf.format(new Date());
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + m_curentDateandTime + ".jpg";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception p_e) {
        }
        return m_imgUri;
    }

    private Uri getVideoUri() {
        Uri m_imgUri = null;
        File m_file;
        try {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String m_curentDateandTime = m_sdf.format(new Date());
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + m_curentDateandTime + ".mp4";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception p_e) {
        }
        return m_imgUri;
    }

    public String getPath(Uri uri) {
        int column_index;
        String imagePath;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    private List<String> getSelectedVideos(int requestCode, Intent data) {

        List<String> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = getPath(this, videoURI);
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            String filePath = getPath(this, videoURI);
            result.add(filePath);
        }

        return result;
    }

    private List<PostVideo> getSelectedVideosPath(int requestCode, Intent data) throws IOException {

        List<PostVideo> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            ArrayList<String> tempMedia = new ArrayList<>();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = getPath(this, videoURI);
                String videoPath = "file://" + filePath;
                //base64File = getBase64(filePath);

                videoExtension = videoPath.substring(videoPath.lastIndexOf(".") + 1);

                Log.d("Extension: ", videoExtension);
                String strMD5 = getMD5EncryptedString(videoPath);
                fileEncoded = strMD5;

                if (videoList.size() == 0) {
                    Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "2", strMD5);
                    sendIsDuplicateVideoRequest(call, videoPath, fileEncoded, filePath);
                    tempMedia.add(videoPath);
                } else {
                    boolean hasAlready = false;
                    for (String temp : videoList) {
                        if (temp.equalsIgnoreCase(videoPath)) {
                            hasAlready = true;

                            //    Tools.toast(PostNew.this, "You have already add this!", R.drawable.ic_info_outline_blue_24dp);
                            break;
                        }
                    }
                    if (!hasAlready) {
                        Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "2", strMD5);
                        sendIsDuplicateVideoRequest(call, videoPath, fileEncoded, filePath);
                        tempMedia.add(videoPath);

                    }
                }


//                Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "2", strMD5);
//                sendIsDuplicateVideoRequest(call);

                //   result.add(new PostVideo(videoPath));

                File file = new File(filePath);
                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
//                Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, true);
//                addPhotoRequest(mediaCall);
            }
            videoList.addAll(tempMedia);
        } else {
            Uri videoURI = data.getData();
            videoFilePath = getPath(this, videoURI);
            String videoPath = "file://" + videoFilePath;

            //  base64File = getBase64(videoFilePath);
            videoExtension = videoPath.substring(videoPath.lastIndexOf(".") + 1);

            Log.d("Extension: ", videoExtension);

            String strMD5 = getMD5EncryptedString(videoPath);
            fileEncoded = strMD5;

            if (videoList.size() == 0) {
                Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "2", strMD5);
                sendIsDuplicateVideoRequest(call, videoPath, fileEncoded, videoFilePath);
                videoList.add(videoPath);
            } else {
                for (String temp : videoList) {
                    if (temp.equalsIgnoreCase(videoPath)) {

                        //  Tools.toast(PostNew.this, "You have already add this!", R.drawable.ic_info_outline_blue_24dp);
                    } else {

                        Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "2", strMD5);
                        sendIsDuplicateVideoRequest(call, videoPath, fileEncoded, videoFilePath);
                    }
                }
            }


            File file = new File(videoFilePath);
            //Parsing any Media type file
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        }

        return result;
    }

    private void sendIsDuplicateVideoRequest(Call<String> call, String videoPath, String fileEncoded, String filePath) {

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {

                                // String message = "You have already posted it .";
                                //Tools.showCustomToast(PostNew.this, mView, message, Gravity.CENTER);
                                progressDialog.show();
                                progressDialog.dismiss();
                                // String videoPath = "file://" + filePath;
                                PostVideo postVideo = new PostVideo();
                                postVideo.setVideoPath(videoPath);
                                postVideo.setMdFive(fileEncoded);
                                postVideo.setDuplicate(true);
                                postVideos.add(postVideo);
                                if (postVideos.size() > 0)
                                    rvMediaShow = true;
                                mediaRecyclerViewToggle();
                                mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
                                mediaRecyclerView.setAdapter(mediaAdapter);

                            } else {

                                if (!isNullOrEmpty(filePath)) {
                                    File file = new File(filePath);
                                    //Parsing any Media type file
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
                                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
                                    Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                                    sendUploadVideoRequest(mediaCall);

                                    progressDialog.show();
                                    // String videoPath = "file://" + filePath;
                                    PostVideo postVideo = new PostVideo();
                                    postVideo.setVideoPath(videoPath);
                                    postVideo.setMdFive(fileEncoded);
                                    postVideo.setDuplicate(false);
                                    postVideos.add(postVideo);
                                    if (postVideos.size() > 0)
                                        rvMediaShow = true;
                                    mediaRecyclerViewToggle();
                                    mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
                                    mediaRecyclerView.setAdapter(mediaAdapter);

                                }

                                //     String message = "Add gallery successfully!";
                                //     Tools.showCustomToast(PostNew.this, mView, message, Gravity.CENTER);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public Set<String> findDuplicates(List<String> listContainingDuplicates) {
        final Set<String> setToReturn = new HashSet<>();
        final Set<String> set1 = new HashSet<>();

        for (String yourInt : listContainingDuplicates) {
            if (!set1.add(yourInt)) {
                setToReturn.add(yourInt);
            }
        }
        return setToReturn;
    }

    private List<PostImage> getSelectedImagesPath(int requestCode, Intent data) throws FileNotFoundException {

        List<PostImage> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            ArrayList<String> tempMedia = new ArrayList<>();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                imageFilePath = getPath(this, videoURI);
                //   String multiImagePath=getPath(this, videoURI);
                String imagePath = "file://" + imageFilePath;
                //     String strBase64 = getBase64(imagePath);
                String strMD5 = getMD5EncryptedString(imagePath);
                fileEncoded = strMD5;

                if (mediaList.size() == 0) {


                    Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
                    sendIsDuplicateImageRequest(call, imagePath, fileEncoded, imageFilePath);
                    tempMedia.add(imagePath);

                } else {
                    boolean hasAlready = false;
                    for (String temp : mediaList) {
                        if (temp.equalsIgnoreCase(imagePath)) {
                            hasAlready = true;

                            //  Tools.toast(PostNew.this, "You have already add this!", R.drawable.ic_info_outline_blue_24dp);
                            break;
                        }
                    }
                    if (!hasAlready) {
                        Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
                        sendIsDuplicateImageRequest(call, imagePath, fileEncoded, imageFilePath);
                        tempMedia.add(imagePath);

                    }
                }


            }
            mediaList.addAll(tempMedia);
        } else {


            Uri videoURI = data.getData();
            imageFilePath = getPath(this, videoURI);
            String imagePath = "file://" + imageFilePath;
            String strMD5 = getMD5EncryptedString(imagePath);
            fileEncoded = strMD5;
            //   String strBase64 = getBase64(imageFilePath);

            if (mediaList.size() == 0) {
                Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
                sendIsDuplicateImageRequest(call, imagePath, fileEncoded, imageFilePath);
                mediaList.add(imagePath);
            } else {
                for (String temp : mediaList) {
                    if (temp.equalsIgnoreCase(imagePath)) {

                        // Tools.toast(PostNew.this, "You have already add this!", R.drawable.ic_info_outline_blue_24dp);
                    } else {

                        Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
                        sendIsDuplicateImageRequest(call, imagePath, fileEncoded, imageFilePath);
                    }
                }
            }

        }

        return result;
    }


    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }


            output64.close();


            encodedFile = output.toString();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    private void sendIsDuplicateImageRequest(Call<String> call, String imagePath, String fileEncoded, String imageFilePath) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {

                                // String message = "You have already posted it .";
                                //Tools.showCustomToast(PostNew.this, mView, message, Gravity.CENTER);

                                postImages.add(new PostImage(imagePath, "", fileEncoded, true));
                                rvMediaShow = true;
                                mediaRecyclerViewToggle();
                                mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
                                mediaRecyclerView.setAdapter(mediaAdapter);
                                progressView.setVisibility(View.GONE);
                                progressView.stopAnimation();

                            } else {

                                if (!isNullOrEmpty(imageFilePath)) {
                                    File file = new File(imageFilePath);
                                    //Parsing any Media type file
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
                                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                                    Call<String> mediaCall = webService.addPhoto(deviceId, profileId, token, fileToUpload);
                                    addPhotoRequest(mediaCall, fileEncoded);
                                    progressDialog.show();
                                    //  progressDialog.setCancelable(false);
                                    postImages.add(new PostImage(imagePath, "", fileEncoded, false));
                                    if (postImages.size() > 0)
                                        rvMediaShow = true;
                                    mediaRecyclerViewToggle();
                                    mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
                                    mediaRecyclerView.setAdapter(mediaAdapter);
                                    progressView.setVisibility(View.GONE);
                                    progressView.stopAnimation();

                                }

//                                String message = "Add gallery successfully!";
//                                Tools.showCustomToast(PostNew.this, mView, message, Gravity.CENTER);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void sendUploadVideoRequest(Call<String> mediaCall) {

        mediaCall.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String video_name = object.getString("video_name");
                            String duration = object.getString("duration");
                            String image_name = object.getString("image_name");
                            String large_image_name = object.getString("large_image_name");

                            //      MultipleMediaFile mediaFile = new MultipleMediaFile();
                            mediaFile.setBase64Md5(fileEncoded);
                            mediaFile.setDuration(duration);
                            mediaFile.setFileType("video");
                            mediaFile.setImageName(image_name);
                            mediaFile.setLargeImageName(large_image_name);
                            mediaFile.setName(video_name);
                            multipleMediaFiles.add(mediaFile);
                            PostVideo postVideo = new PostVideo();
                            postVideo.setMultipleMediaFile(mediaFile);
                            Gson gson = new Gson();
                            String gsonString = gson.toJson(mediaFile);
                            mediaFiles.add(gsonString);

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                if(uri == null)
                    throw new IllegalArgumentException("The filename cannot be null!");
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                if(uri == null)
                    throw new IllegalArgumentException("The filename cannot be null!");

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            if(uri == null)
                throw new IllegalArgumentException("The filename cannot be null!");
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private View mainView;
    private LinearLayout linearLayout;
    private View loading;
    private ImageView imageView;

    private LinkPreviewCallback callback = new LinkPreviewCallback() {
        /**
         * This view is used to be updated or added in the layout after getting
         * the result
         */
//        private View mainView;
//        private LinearLayout linearLayout;
//        private View loading;
//        private ImageView imageView;
        @Override
        public void onPre() {
            hideSoftKeyboard();

            currentImageSet = null;
            currentItem = 0;

//            tvSubmitPost.setVisibility(View.GONE);


            currentImage = null;
            noThumb = false;
            currentTitle = currentDescription = currentUrl = currentCannonicalUrl = "";

//            tvSubmitPost.setEnabled(false);
//            tvSubmitPost.setEnabled(true);

            /** Inflating the preview layout */
            mainView = getLayoutInflater().inflate(R.layout.main_view, null);

            linearLayout = (LinearLayout) mainView.findViewById(R.id.external);

            /**
             * Inflating a loading layout into Main View LinearLayout
             */
            loading = getLayoutInflater().inflate(R.layout.loading,
                    linearLayout);

            dropPreview.addView(mainView);
        }

        @Override
        public void onPos(final SourceContent sourceContent, boolean isNull) {

            /** Removing the loading layout */
            linearLayout.removeAllViews();

            if (isNull || sourceContent.getFinalUrl().equals("")) {
                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                View failed = getLayoutInflater().inflate(R.layout.failed,
                        linearLayout);

                TextView titleTextView = (TextView) failed
                        .findViewById(R.id.text);
                titleTextView.setText(getString(R.string.failed_preview) + "\n"
                        + sourceContent.getFinalUrl());

                failed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        releasePreviewArea();
                        editPostMessage.setText("");
                        editPostMessage.getText().clear();
                    }
                });

            } else {
                linkscrapting(sourceContent);
            }

            currentTitle = sourceContent.getTitle();
            contentLinkTitle = currentTitle;
            currentDescription = sourceContent.getDescription();
            contentLinkDesc = currentDescription;
            currentUrl = sourceContent.getUrl();
            contentLinkUrl = currentUrl;
            currentCannonicalUrl = sourceContent.getCannonicalUrl();
            contentHost = currentCannonicalUrl;
            List<String> imageUrl = sourceContent.getImages();
            for (String temp : imageUrl
            ) {
                contentLinkImage = temp;
            }

            LinkScriptItem scriptItem = new LinkScriptItem(currentImage, currentTitle, currentDescription, currentUrl);
            scriptItemList.add(scriptItem);
            LinkScriptAdapter linkScriptAdapter = new LinkScriptAdapter(PostNew.this, scriptItemList);
            rvLinkScript.setAdapter(linkScriptAdapter);
        }

        private void linkscrapting(SourceContent sourceContent) {
            //  postButton.setVisibility(View.VISIBLE);
            postButton.setVisibility(View.GONE);
            //  tvSubmitPost.setVisibility(View.VISIBLE);
            currentImageSet = new Bitmap[sourceContent.getImages().size()];

            /**
             * Inflating the content layout into Main View LinearLayout
             */
            final View content = getLayoutInflater().inflate(
                    R.layout.preview_content, linearLayout);

            /** Fullfilling the content layout */
            final LinearLayout infoWrap = (LinearLayout) content
                    .findViewById(R.id.info_wrap);
            final LinearLayout titleWrap = (LinearLayout) infoWrap
                    .findViewById(R.id.title_wrap);
            final LinearLayout thumbnailOptions = (LinearLayout) content
                    .findViewById(R.id.thumbnail_options);


            final ImageView imageSet = (ImageView) content
                    .findViewById(R.id.image_post_set);

            // final TextView close = (TextView) titleWrap.findViewById(R.id.close);
            final FloatingActionButton close = (FloatingActionButton) findViewById(R.id.close);
            final TextView titleTextView = (TextView) titleWrap
                    .findViewById(R.id.title);
            final EditText titleEditText = (EditText) titleWrap
                    .findViewById(R.id.input_title);
            final TextView urlTextView = (TextView) content
                    .findViewById(R.id.url);
            final TextView descriptionTextView = (TextView) content
                    .findViewById(R.id.description);
            final EditText descriptionEditText = (EditText) content
                    .findViewById(R.id.input_description);
            final TextView countTextView = (TextView) thumbnailOptions
                    .findViewById(R.id.count);

            final Button previousButton = (Button) thumbnailOptions
                    .findViewById(R.id.post_previous);
            final Button forwardButton = (Button) thumbnailOptions
                    .findViewById(R.id.post_forward);

            editTextTitlePost = titleEditText;
            editTextDescriptionPost = descriptionEditText;

            titleTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    titleTextView.setVisibility(View.GONE);

                    titleEditText.setText(TextCrawler
                            .extendedTrim(titleTextView.getText()
                                    .toString()));
                    titleEditText.setVisibility(View.VISIBLE);
                }
            });
            titleEditText
                    .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                        @Override
                        public boolean onEditorAction(TextView arg0,
                                                      int arg1, KeyEvent arg2) {

                            if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                titleEditText.setVisibility(View.GONE);

                                currentTitle = TextCrawler
                                        .extendedTrim(titleEditText
                                                .getText().toString());

                                titleTextView.setText(currentTitle);
                                titleTextView.setVisibility(View.VISIBLE);

                                hideSoftKeyboard();
                            }

                            return false;
                        }
                    });
            descriptionTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    descriptionTextView.setVisibility(View.GONE);

                    descriptionEditText.setText(TextCrawler
                            .extendedTrim(descriptionTextView.getText()
                                    .toString()));
                    descriptionEditText.setVisibility(View.VISIBLE);
                }
            });
            descriptionEditText
                    .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                        @Override
                        public boolean onEditorAction(TextView arg0,
                                                      int arg1, KeyEvent arg2) {

                            if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                descriptionEditText
                                        .setVisibility(View.GONE);

                                currentDescription = TextCrawler
                                        .extendedTrim(descriptionEditText
                                                .getText().toString());

                                descriptionTextView
                                        .setText(currentDescription);
                                descriptionTextView
                                        .setVisibility(View.VISIBLE);

                                hideSoftKeyboard();
                            }

                            return false;
                        }
                    });

            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    releasePreviewArea();
                }
            });


            previousButton.setEnabled(false);
            previousButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (currentItem > 0)
                        changeImage(previousButton, forwardButton,
                                currentItem - 1, sourceContent,
                                countTextView, imageSet, sourceContent
                                        .getImages().get(currentItem - 1),
                                currentItem);
                }
            });
            forwardButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (currentItem < sourceContent.getImages().size() - 1)
                        changeImage(previousButton, forwardButton,
                                currentItem + 1, sourceContent,
                                countTextView, imageSet, sourceContent
                                        .getImages().get(currentItem + 1),
                                currentItem);
                }
            });

            if (sourceContent.getImages().size() > 0) {

                if (sourceContent.getImages().size() > 1) {
                    countTextView.setText("1 " + getString(R.string.of)
                            + " " + sourceContent.getImages().size());

                    thumbnailOptions.setVisibility(View.VISIBLE);
                }


                UrlImageViewHelper.setUrlDrawable(imageSet, sourceContent
                        .getImages().get(0), new UrlImageViewCallback() {

                    @Override
                    public void onLoaded(ImageView imageView,
                                         Bitmap loadedBitmap, String url,
                                         boolean loadedFromCache) {
                        if (loadedBitmap != null) {
                            currentImage = loadedBitmap;
                            currentImageSet[0] = loadedBitmap;
                        }
                    }
                });

            } else {

            }

            if (sourceContent.getTitle().equals(""))
                sourceContent.setTitle(getString(R.string.enter_title));
            if (sourceContent.getDescription().equals(""))
                sourceContent
                        .setDescription(getString(R.string.enter_description));

            titleTextView.setText(sourceContent.getTitle());
            urlTextView.setText(sourceContent.getCannonicalUrl());
            descriptionTextView.setText(sourceContent.getDescription());

            //   postButton.setVisibility(View.VISIBLE);
            postButton.setVisibility(View.GONE);
        }


    };

    private void urlscrapting(LinkScrapItem sourceContent) {

        hideSoftKeyboard();

        currentImageSet = null;
        currentItem = 0;

//            tvSubmitPost.setVisibility(View.GONE);


        currentImage = null;
        noThumb = false;
        currentTitle = currentDescription = currentUrl = currentCannonicalUrl = "";

//            tvSubmitPost.setEnabled(false);
//            tvSubmitPost.setEnabled(true);

        /** Inflating the preview layout */
        mainView = getLayoutInflater().inflate(R.layout.main_view, null);

        linearLayout = (LinearLayout) mainView.findViewById(R.id.external);

        /**
         * Inflating a loading layout into Main View LinearLayout
         */
        loading = getLayoutInflater().inflate(R.layout.loading,
                linearLayout);

        dropPreview.addView(mainView);
        linearLayout.removeAllViews();


        //  postButton.setVisibility(View.VISIBLE);
        postButton.setVisibility(View.GONE);
        //  tvSubmitPost.setVisibility(View.VISIBLE);
        currentImageSet = new Bitmap[sourceContent.getImages().size()];

        /**
         * Inflating the content layout into Main View LinearLayout
         */
        final View content = getLayoutInflater().inflate(
                R.layout.preview_content, linearLayout);

        /** Fullfilling the content layout */
        final LinearLayout infoWrap = (LinearLayout) content
                .findViewById(R.id.info_wrap);
        final LinearLayout titleWrap = (LinearLayout) infoWrap
                .findViewById(R.id.title_wrap);
        final LinearLayout thumbnailOptions = (LinearLayout) content
                .findViewById(R.id.thumbnail_options);


        final ImageView imageSet = (ImageView) content
                .findViewById(R.id.image_post_set);

        // final TextView close = (TextView) titleWrap.findViewById(R.id.close);
        final FloatingActionButton close = (FloatingActionButton) findViewById(R.id.close);
        final TextView titleTextView = (TextView) titleWrap
                .findViewById(R.id.title);
        final EditText titleEditText = (EditText) titleWrap
                .findViewById(R.id.input_title);
        final TextView urlTextView = (TextView) content
                .findViewById(R.id.url);
        final TextView descriptionTextView = (TextView) content
                .findViewById(R.id.description);
        final EditText descriptionEditText = (EditText) content
                .findViewById(R.id.input_description);
        final TextView countTextView = (TextView) thumbnailOptions
                .findViewById(R.id.count);

        final Button previousButton = (Button) thumbnailOptions
                .findViewById(R.id.post_previous);
        final Button forwardButton = (Button) thumbnailOptions
                .findViewById(R.id.post_forward);

        editTextTitlePost = titleEditText;
        editTextDescriptionPost = descriptionEditText;

        titleTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                titleTextView.setVisibility(View.GONE);

                titleEditText.setText(TextCrawler
                        .extendedTrim(titleTextView.getText()
                                .toString()));
                titleEditText.setVisibility(View.VISIBLE);
            }
        });
        titleEditText
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView arg0,
                                                  int arg1, KeyEvent arg2) {

                        if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            titleEditText.setVisibility(View.GONE);

                            currentTitle = TextCrawler
                                    .extendedTrim(titleEditText
                                            .getText().toString());

                            titleTextView.setText(currentTitle);
                            titleTextView.setVisibility(View.VISIBLE);

                            hideSoftKeyboard();
                        }

                        return false;
                    }
                });
        descriptionTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                descriptionTextView.setVisibility(View.GONE);

                descriptionEditText.setText(TextCrawler
                        .extendedTrim(descriptionTextView.getText()
                                .toString()));
                descriptionEditText.setVisibility(View.VISIBLE);
            }
        });
        descriptionEditText
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView arg0,
                                                  int arg1, KeyEvent arg2) {

                        if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            descriptionEditText
                                    .setVisibility(View.GONE);

                            currentDescription = TextCrawler
                                    .extendedTrim(descriptionEditText
                                            .getText().toString());

                            descriptionTextView
                                    .setText(currentDescription);
                            descriptionTextView
                                    .setVisibility(View.VISIBLE);

                            hideSoftKeyboard();
                        }

                        return false;
                    }
                });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                releasePreviewArea();
                contentType = 1;
                status = 1;

            }
        });


        previousButton.setEnabled(false);
        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentItem > 0)
                    changeUrlImage(previousButton, forwardButton,
                            currentItem - 1, sourceContent,
                            countTextView, imageSet, sourceContent
                                    .getImages().get(currentItem - 1),
                            currentItem);
            }
        });
        forwardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentItem < sourceContent.getImages().size() - 1)
                    changeUrlImage(previousButton, forwardButton,
                            currentItem + 1, sourceContent,
                            countTextView, imageSet, sourceContent
                                    .getImages().get(currentItem + 1),
                            currentItem);
            }
        });

        if (sourceContent.getImages().size() > 0) {

            if (sourceContent.getImages().size() > 1) {
                countTextView.setText("1 " + getString(R.string.of)
                        + " " + sourceContent.getImages().size());

                thumbnailOptions.setVisibility(View.VISIBLE);
            }


            UrlImageViewHelper.setUrlDrawable(imageSet, sourceContent
                    .getImages().get(0).getImg(), new UrlImageViewCallback() {

                @Override
                public void onLoaded(ImageView imageView,
                                     Bitmap loadedBitmap, String url,
                                     boolean loadedFromCache) {
                    if (loadedBitmap != null) {
                        currentImage = loadedBitmap;
                        currentImageSet[0] = loadedBitmap;
                    }
                }
            });

        } else {

        }

        if (sourceContent.getTitle().equals(""))
            sourceContent.setTitle(getString(R.string.enter_title));
        if (sourceContent.getDescription().equals(""))
            sourceContent
                    .setDescription(getString(R.string.enter_description));

        titleTextView.setText(sourceContent.getTitle());
//            urlTextView.setText(sourceContent.getCannonicalUrl());
        urlTextView.setText(sourceContent.getHost());
        descriptionTextView.setText(sourceContent.getDescription());

        //   postButton.setVisibility(View.VISIBLE);
        postButton.setVisibility(View.GONE);

        currentTitle = sourceContent.getTitle();
        contentLinkTitle = currentTitle;
        currentDescription = sourceContent.getDescription();
        contentLinkDesc = currentDescription;
        currentUrl = sourceContent.getUrl();
        contentLinkUrl = currentUrl;
        currentCannonicalUrl = sourceContent.getHost();
        contentHost = currentCannonicalUrl;

        for (ImageItem temp : sourceContent.getImages()) {
            contentLinkImage = temp.getImg();
        }

        LinkScriptItem scriptItem = new LinkScriptItem(currentImage, currentTitle, currentDescription, currentUrl);
        scriptItemList.add(scriptItem);
        LinkScriptAdapter linkScriptAdapter = new LinkScriptAdapter(PostNew.this, scriptItemList);
        rvLinkScript.setAdapter(linkScriptAdapter);
    }

    /**
     * Hide keyboard
     */
    private void hideSoftKeyboard() {
        hideSoftKeyboard(editPostMessage);

        if (editTextTitlePost != null)
            hideSoftKeyboard(editTextTitlePost);
        if (editTextDescriptionPost != null)
            hideSoftKeyboard(editTextDescriptionPost);
    }

    private void hideSoftKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void releasePreviewArea() {
        tvSubmitPost.setEnabled(true);
        tvSubmitPost.setVisibility(View.VISIBLE);
        postButton.setVisibility(View.GONE);
        dropPreview.removeAllViews();
    }


    /**
     * Change the current image in image set
     */
    private void changeImage(Button previousButton, Button forwardButton,
                             final int index, SourceContent sourceContent,
                             TextView countTextView, ImageView imageSet, String url,
                             final int current) {

        if (currentImageSet[index] != null) {
            currentImage = currentImageSet[index];
            imageSet.setImageBitmap(currentImage);
        } else {
            UrlImageViewHelper.setUrlDrawable(imageSet, url,
                    new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                currentImageSet[index] = loadedBitmap;
                            }
                        }
                    });

        }

        currentItem = index;

        if (index == 0)
            previousButton.setEnabled(false);
        else
            previousButton.setEnabled(true);

        if (index == sourceContent.getImages().size() - 1)
            forwardButton.setEnabled(false);
        else
            forwardButton.setEnabled(true);

        countTextView.setText((index + 1) + " " + getString(R.string.of) + " "
                + sourceContent.getImages().size());
    }


    /**
     * Change the current image in image set
     */
    private void changeUrlImage(Button previousButton, Button forwardButton,
                                final int index, LinkScrapItem sourceContent,
                                TextView countTextView, ImageView imageSet, ImageItem imageItem,
                                final int current) {

        String url = imageItem.getImg();
        if (currentImageSet[index] != null) {
            currentImage = currentImageSet[index];
            imageSet.setImageBitmap(currentImage);
        } else {
            UrlImageViewHelper.setUrlDrawable(imageSet, url,
                    new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                currentImageSet[index] = loadedBitmap;
                            }
                        }
                    });

        }

        currentItem = index;

        if (index == 0)
            previousButton.setEnabled(false);
        else
            previousButton.setEnabled(true);

        if (index == sourceContent.getImages().size() - 1)
            forwardButton.setEnabled(false);
        else
            forwardButton.setEnabled(true);

        countTextView.setText((index + 1) + " " + getString(R.string.of) + " "
                + sourceContent.getImages().size());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public void onPositiveResult(DialogFragment dlg) {
        //addedPostContributor
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<String> call = webService.addedPostContributor(deviceId, profileId, token, categoryId, subCategoryId, 5, userIds);
            addedPostContributorRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    private void addedPostContributorRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String status = object.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                if (contentType == 5) {
                                    Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                                    sendVideoRequest(mediaCall);
                                } else {
//                                    Intent intent = new Intent(PostNew.this, Home.class);
//                                    intent.putExtra("STATUS", status);
//                                    startActivity(intent);
                                    finish();
                                    sendBroadcast((new Intent()).setAction(AppConstants.NEW_POST_ADD_BROADCAST));
                                    String message = "You are now a contributor to the Hobby & Leisure - Airplanes category and your post has been added to your profile.";
                                    //  Tools.showCustomToast(PostNew.this, mView, message, Gravity.CENTER);
                                }

                            } else {
                                Tools.showCustomToast(PostNew.this, mView, status, Gravity.CENTER);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void onNegativeResult(DialogFragment dlg) {

        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<String> call = webService.addedPostContributor(deviceId, profileId, token, categoryId, subCategoryId, 6, userIds);
            addedPostContributorRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    //Your post has been successfully added to your profile. View Post
    @Override
    public void onNeutralResult(DialogFragment dlg) {

    }


    @Override
    public void onCameraClicked() {

        rvMimShow = false;
        rvMimToggle();
        if (isGrantCamera) {
            sendImageFromCamera();
        } else {
            checkCameraPermission();
        }
    }

    @Override
    public void onVideoLibraryClicked() {

        rvMimShow = false;
        rvMimToggle();
        checkVideoPermission();
    }

    @Override
    public void onVideoRecordClicked() {


        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //  Uri videoUri=getVideoUri();
        // videoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
        }

//        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takeVideoIntent,
//                    REQUEST_VIDEO_CAPTURE);
//        }

    }

    @Override
    public void onPhotoClicked() {

        rvMimShow = false;
        rvMimToggle();
        if (isGrantGallery) {

            sendImageFromGallery();
        } else {
            checkGalleryPermission();
        }

    }

    @Override
    public void onEmojiClicked() {

    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (uri != null) {
            if (EasyPermissions.hasPermissions(PostNew.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                displayRecordedVideo.setVideoURI(uri);
//                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, PostNew.this);
                //    Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);
                //Store the video to your server
                //  uploadVideoToServer(pathToStoredVideo);

                File file = new File(pathToStoredVideo);
                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
                Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                sendUploadVideoRequest(mediaCall);

                progressDialog.show();

                String videoPath = "file://" + pathToStoredVideo;
                PostVideo postVideo = new PostVideo();
                postVideo.setVideoPath(videoPath);
                postVideo.setMdFive(fileEncoded);
                postVideo.setDuplicate(false);
                postVideos.add(postVideo);
                if (postVideos.size() > 0)
                    rvMediaShow = true;
                mediaRecyclerViewToggle();
                mediaAdapter = new MediaAdapter(mContext, postImages, postVideos, imageListener, videoListen);
                mediaRecyclerView.setAdapter(mediaAdapter);
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "User has denied requested permission");
    }


}
