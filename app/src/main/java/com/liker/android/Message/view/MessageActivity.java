package com.liker.android.Message.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liker.android.Message.model.FriendInfo;
import com.liker.android.R;


import static com.liker.android.Tool.AppConstants.IN_CHAT_MODE;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        IN_CHAT_MODE = true;

        boolean isMessageFromProfile = getIntent().getBooleanExtra("messageFromProfile", false);
        initialFragment(isMessageFromProfile);
    }

    private void initialFragment(boolean isMessageFromProfile) {
        if (isMessageFromProfile) {
            FriendInfo friendInfo = (FriendInfo) getIntent().getSerializableExtra("friendInfo");
            Bundle bundle = new Bundle();
            bundle.putSerializable("friend_info", friendInfo);
            MessagingFragment messagingFragment = new MessagingFragment();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            messagingFragment.setArguments(bundle);
            transaction.replace(R.id.container, messagingFragment).commit();
        } else {
            MessageListFragment messageListFragment = new MessageListFragment();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, messageListFragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IN_CHAT_MODE = false;
    }

}
