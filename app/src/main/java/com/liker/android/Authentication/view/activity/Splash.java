package com.liker.android.Authentication.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.liker.android.Home.view.activity.Home;
import com.liker.android.Tool.PrefManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

//import com.doodle.Home.view.activity.Home;
//import com.doodle.Tool.PrefManager;

public class Splash extends AppCompatActivity {


    private static final String TAG = "Splash_KEY";
    PrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        manager = new PrefManager(this);
        printHashKey(this);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //   setContentView(R.layout.activity_splash);

//        Handler handler=new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(Splash.this, ForgotPasswords.class));
//                finish();
//            }
//        },2000);
        if (manager.getProfileId().isEmpty()) {
         //   startActivity(new Intent(Splash.this, ForgotPasswords.class));
            startActivity(new Intent(Splash.this, Welcome.class));
        } else {
            startActivity(new Intent(Splash.this, Home.class));
        }

//        startActivity(new Intent(Splash.this, ProfileActivity.class));

        finish();


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
}
