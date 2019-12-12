package com.liker.android.Tool.Service;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        Log.i("onEmptyResponse", "Returned empty response");
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Log.i("onEmptyResponse", "Returned empty response");
    }



}
