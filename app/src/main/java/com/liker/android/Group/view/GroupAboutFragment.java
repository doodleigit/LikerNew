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

import com.liker.android.R;
import com.liker.android.Tool.AppSingleton;

import static com.liker.android.Tool.Tools.isEmpty;
import static com.liker.android.Tool.Tools.isNullOrEmpty;

public class GroupAboutFragment extends Fragment implements View.OnClickListener {
    View view;

    TextView tvGroupAbout;
    ImageView imgGroupAboutEdit;
    private String groupAboutMessage;

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
        ownPageCheck();
    }

    private void initialAllClickListener() {


    }

    private void ownPageCheck() {

        boolean isMember= AppSingleton.getInstance().isMember();
        if(isMember){
            imgGroupAboutEdit.setVisibility(View.VISIBLE);
           if(isNullOrEmpty(groupAboutMessage)) {
               tvGroupAbout.setText(getText(R.string.add_group_description));
               groupAboutMessage=tvGroupAbout.getText().toString();
           }else {
               groupAboutMessage=tvGroupAbout.getText().toString();
           }
        }else {
            imgGroupAboutEdit.setVisibility(View.INVISIBLE);
        }

    }

    private void setData() {

        if(!isEmpty(AppSingleton.getInstance().getPageAboutDescription())){
            tvGroupAbout.setText(groupAboutMessage);
        }else {
            tvGroupAbout.setText("");
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgGroupAboutEdit:
//                GroupAboutEditDialog dialog = (GroupAboutEditDialog) GroupAboutEditDialog.instantiate(getActivity(), "Hello world");
//                dialog.show(getFragmentManager(), "dialog");
             //   GroupAboutEditDialog dialog = new GroupAboutEditDialog ();
                GroupAboutEditDialog dialog = GroupAboutEditDialog.newInstance(groupAboutMessage);
                dialog .show(getFragmentManager(), "GroupAboutEditDialog");
                break;
        }
    }
}
