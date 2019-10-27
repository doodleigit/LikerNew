package com.liker.android.Setting.view;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.liker.android.R;


public class SettingActivity extends AppCompatActivity {

    String settingType, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialComponent();
    }

    private void initialComponent() {
        settingType = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("link");

        switch (settingType) {
            case "account":
                initialFragment(new AccountSettingFragment());
                break;
            case "findFriends":
                initialFragment(new FindFriendsFragment());
                break;
            case "privacy":
                initialFragment(new PrivacyAndSecurityFragment());
                break;
            case "notification":
                initialFragment(new NewNotificationSettingFragment());
                break;
            case "contributor":
                initialFragment(new ContributorSettingFragment());
                break;
            default:
                initialFragment(new SettingWebViewFragment());
                break;
        }
    }

    private void initialFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("title", settingType);
        bundle.putString("link", url);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }
}
