package com.liker.android.Authentication.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.doodle.Authentication.model.LoginInfo;
//import com.doodle.Authentication.model.SocialInfo;
//import com.doodle.Home.view.activity.Home;
//import com.doodle.R;
//import com.doodle.Tool.PrefManager;
import com.liker.android.Authentication.model.LoginInfo;
import com.liker.android.Authentication.model.SocialInfo;
import com.liker.android.Authentication.service.AuthService;
import com.liker.android.Home.view.activity.Home;
import com.liker.android.R;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.liker.android.Authentication.view.activity.Login.SOCIAL_ITEM;

//import static com.doodle.Authentication.view.activity.Login.SOCIAL_ITEM;
//import static com.doodle.Tool.AppConstants.PROFILE_IMAGE;

public class LoginAgain extends AppCompatActivity implements View.OnClickListener {

    private PrefManager manager;
    private ImageView profileImage;
    private TextView tvProfileName;
    private ProgressBar progressBar;

    private AlertDialog.Builder alertDialog;

    private AuthService authService;
    private LoginInfo loginInfo;

    private boolean isLogging = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_again);

        initialComponent();

        authService = AuthService.retrofitBase.create(AuthService.class);
        manager = new PrefManager(this);
        loginInfo = (LoginInfo) getIntent().getSerializableExtra("login_info");
        if (loginInfo == null) {
            throw new AssertionError("Null data item received!");
        }
        findViewById(R.id.tvLoginPage).setOnClickListener(this);
        findViewById(R.id.tvSignUpPage).setOnClickListener(this);
        findViewById(R.id.profile_layout).setOnClickListener(this);
        profileImage = findViewById(R.id.profile_image);
        tvProfileName = findViewById(R.id.tvProfileName);
        progressBar = findViewById(R.id.progress_bar);

        String image_url = loginInfo.getProfileImage();
        String profileName = loginInfo.getProfileName();

        if (image_url != null && image_url.length() > 0) {
            Picasso.with(LoginAgain.this)
                    .load(image_url)
                    .placeholder(R.drawable.profile)
                    .into(profileImage);

        }
        if (profileName != null && profileName.length() > 0) {
            tvProfileName.setText(profileName);
        }

    }

    private void initialComponent() {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.please_check_your_network_connection))
                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
                            if (isLogging) {
                                isLogging = false;
                                regenerateToken();
                            }
                        } else {
                            showAlert(getString(R.string.please_check_your_network_connection));
                        }

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.tvLoginPage:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.tvSignUpPage:
                SocialInfo info = new SocialInfo("", "", "", "", "", "", "");
                Intent intent = new Intent(this, Signup.class);
                intent.putExtra(SOCIAL_ITEM, info);
                startActivity(intent);
                break;
            case R.id.profile_layout:
                if (isLogging) {
                    isLogging = false;
                    regenerateToken();
                }
                break;
        }

    }

    private void showAlert(String message) {
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    private void regenerateToken() {
        progressBar.setVisibility(View.VISIBLE);
        Call<String> call = authService.checkToken(loginInfo.getDeviceId(), loginInfo.getToken(), loginInfo.getProfileId(), true);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    boolean status = object.getBoolean("status");
                    if (status) {
                        String token = object.getString("security_token");
                        manager.setUserInfo(loginInfo.getUserInfo());
                        manager.setToken(token);
                        manager.setProfileName(loginInfo.getProfileName());
                        manager.setProfileImage(loginInfo.getProfileImage());
                        manager.setProfileId(loginInfo.getProfileId());
                        manager.setUserName(loginInfo.getUserName());
                        manager.setDeviceId(loginInfo.getDeviceId());
                        startActivity(new Intent(LoginAgain.this, Home.class));
                    }
                    finish();
                } catch (JSONException | NullPointerException ignored) {
                    showAlert(getString(R.string.something_went_wrong));
                }
                isLogging = true;
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isLogging = true;
                showAlert(getString(R.string.the_server_not_responding));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
