package com.liker.android.Group.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liker.android.Group.service.GroupAboutDescriptionEvent;
import com.liker.android.R;
import com.liker.android.Tool.AppSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.liker.android.Tool.Tools.isEmpty;
import static com.liker.android.Tool.Tools.isNullOrEmpty;

public class GroupAboutFragment extends Fragment implements View.OnClickListener {
    private View view;

    private TextView tvGroupAbout;
    private ImageView imgGroupAboutEdit;
    private String groupAboutMessage;
    private String groupId;
    private boolean isMember;


    public static GroupAboutFragment newInstance(String groupId,boolean isMember) {

        Bundle args = new Bundle();
        args.putString("group_id",groupId);
        args.putBoolean("is_member",isMember);
        GroupAboutFragment fragment = new GroupAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argument=getArguments();
        if (argument != null) {
            groupId=argument.getString("group_id");
            isMember=argument.getBoolean("is_member");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_about_layout, container, false);

        initialComponent();
        initialAllClickListener();
        setData();
        return view;

    }

    private void initialComponent() {
        imgGroupAboutEdit = view.findViewById(R.id.imgGroupAboutEdit);
        imgGroupAboutEdit.setOnClickListener(this);
        tvGroupAbout = view.findViewById(R.id.tvGroupAbout);
        EventBus.getDefault().register(this);

    }

    private void initialAllClickListener() {


    }

    private void ownPageCheck() {

      //  boolean isMember = AppSingleton.getInstance().isMember();
        if (isMember) {
            imgGroupAboutEdit.setVisibility(View.VISIBLE);
            if (isNullOrEmpty(groupAboutMessage)) {
                tvGroupAbout.setText(getText(R.string.add_group_description));
                groupAboutMessage = tvGroupAbout.getText().toString();
            } else {
                groupAboutMessage = tvGroupAbout.getText().toString();
            }
        } else {
            imgGroupAboutEdit.setVisibility(View.INVISIBLE);
        }

    }

    private void setData() {

        if (!isEmpty(AppSingleton.getInstance().getPageAboutDescription())) {
            groupAboutMessage = AppSingleton.getInstance().getPageAboutDescription();
            tvGroupAbout.setText(groupAboutMessage);
        } else {
            tvGroupAbout.setText("");
        }
        ownPageCheck();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgGroupAboutEdit:
//                GroupAboutEditDialog dialog = (GroupAboutEditDialog) GroupAboutEditDialog.instantiate(getActivity(), "Hello world");
//                dialog.show(getFragmentManager(), "dialog");
                //   GroupAboutEditDialog dialog = new GroupAboutEditDialog ();
                GroupAboutEditDialog dialog = GroupAboutEditDialog.newInstance(groupAboutMessage,groupId);
                dialog.show(getFragmentManager(), "GroupAboutEditDialog");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode= ThreadMode.MAIN)
    public   void onMessageEvent(GroupAboutDescriptionEvent event){
        AppSingleton.getInstance().setPageAboutDescription(event.getMessage());
        tvGroupAbout.setText(event.getMessage());
    }
}
