package com.liker.android.Group.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.liker.android.R;

//import com.doodle.R;
//import com.doodle.Tool.Tools;


public class GroupDeleteDialog extends DialogFragment {
    private final String TAG = "AUC_CUSTOM";
    private DeleteGroupListener mHost;

    // TODO: Implement an interface for hosts to get callbacks
    public interface DeleteGroupListener {
        public void onDeleteGroup(DialogFragment dlg);
        public void onNo(DialogFragment dlg);

    }

    public static final String MESSAGE_GROUP_NAME_key = "message_group_name_key";

    public static GroupDeleteDialog newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(GroupDeleteDialog.MESSAGE_GROUP_NAME_key, message);
        GroupDeleteDialog fragment = new GroupDeleteDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    String groupName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argument = getArguments();
        if (argument != null) {
            groupName = argument.getString(MESSAGE_GROUP_NAME_key);
        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // TODO: Create the custom layout using the LayoutInflater class
        // LayoutInflater inflater = LayoutInflater.from(getActivity()); //or
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_group, null);
        TextView tvNoThanks = view.findViewById(R.id.tvNo);
        TextView tvRateNow = view.findViewById(R.id.tvYes);
        TextView tvRateInfo = view.findViewById(R.id.tvDeleteInfo);

        String groupInfo="Leave and Delete this group : Are you sure you want to Leave "+groupName+"?"+" As you are the last member, leaving now will also delete the group ";
        int unicode = 0x1F60A;
        String smileUnicode=getEmojiByUnicode(unicode);

        tvRateInfo.setText(groupInfo);

        tvNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Cancel clicked");
                mHost.onNo(GroupDeleteDialog.this);
                dismiss();
            }
        });
        tvRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Cancel clicked");
                mHost.onDeleteGroup(GroupDeleteDialog.this);
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG, "Dialog cancelled!");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = (DeleteGroupListener) context;
    }

    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag).addToBackStack(null);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.e("IllegalStateException", "Exception", e);
        }

    }

}
