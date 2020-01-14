package com.liker.android.Authentication.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.liker.android.Authentication.service.AuthService;
import com.liker.android.Home.view.activity.Home;
import com.liker.android.R;
import com.liker.android.Tool.NetworkHelper;
import com.liker.android.Tool.PrefManager;
import com.liker.android.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.doodle.Home.view.activity.Home;
//import com.doodle.Tool.PrefManager;

public class Splash extends AppCompatActivity {


    private static final String TAG = "Splash_KEY";

    ProgressBar progressBar;

    PrefManager manager;
    AuthService authService;

    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialComponent();
        printHashKey(this);
        if (manager.getProfileId().isEmpty()) {
            startActivity(new Intent(Splash.this, Welcome.class));
        } else {
            if (Tools.isNetworkConnected(this)) {
                checkToken();
            } else {
                showAlert(getString(R.string.please_check_your_network_connection));
            }
        }
    }

    private void initialComponent() {
        Fabric.with(this, new Crashlytics());
        manager = new PrefManager(this);
        authService = AuthService.retrofitBase.create(AuthService.class);
        progressBar = findViewById(R.id.progress_bar);

        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(getString(R.string.please_check_your_network_connection))
                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
                            progressBar.setVisibility(View.VISIBLE);
                            checkToken();
                        } else {
                            showAlert(getString(R.string.please_check_your_network_connection));
                        }

                    }
                })
                .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                });
    }

    private void showAlert(String message) {
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    public  void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void checkToken() {
        Call<String> call = authService.checkToken(manager.getDeviceId(), manager.getToken(), manager.getProfileId(), true);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    boolean status = object.getBoolean("status");
                    if (status) {
                        String token = object.getString("security_token");
                        manager.setToken(token);
                        startActivity(new Intent(Splash.this, Home.class));
                    } else {
                        startActivity(new Intent(Splash.this, Welcome.class));
                    }
                    finish();
                } catch (JSONException | NullPointerException ignored) {
                    showAlert(getString(R.string.something_went_wrong));
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showAlert(getString(R.string.the_server_not_responding));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
