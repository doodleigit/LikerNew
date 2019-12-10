package com.liker.android.Group;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.liker.android.R;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageCancelCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        imageCancelCreateGroup = findViewById(R.id.imageCancelCreateGroup);
        imageCancelCreateGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imageCancelCreateGroup:
                finish();
                break;
        }
    }
}
