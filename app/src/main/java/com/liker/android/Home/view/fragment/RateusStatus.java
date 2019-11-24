package com.liker.android.Home.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.liker.android.R;
import com.liker.android.Tool.Tools;

import static android.graphics.Typeface.BOLD;

//import com.doodle.R;
//import com.doodle.Tool.Tools;


public class RateusStatus extends DialogFragment {
    private final String TAG = "AUC_CUSTOM";
    private RateusStatusListener mHost;

    // TODO: Implement an interface for hosts to get callbacks
    public interface RateusStatusListener {
        public void onSure(DialogFragment dlg);
        public void onNoThanks(DialogFragment dlg);

    }

    public static final String MESSAGE_key = "message_key";

    public static RateusStatus newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(RateusStatus.MESSAGE_key, message);
        RateusStatus fragment = new RateusStatus();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    String message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argument = getArguments();
        if (argument != null) {
            message = argument.getString(MESSAGE_key);
            Log.d(TAG, "onCreateView: " + message);
        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // TODO: Create the custom layout using the LayoutInflater class
        // LayoutInflater inflater = LayoutInflater.from(getActivity()); //or
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rate_us, null);
        TextView tvNoThanks = view.findViewById(R.id.tvNoThanks);
        TextView tvRateNow = view.findViewById(R.id.tvRateNow);
        TextView tvRateInfo = view.findViewById(R.id.tvRateInfo);

        String strInfo="Do you like Liker? ";
        int unicode = 0x1F60A;
        String smileUnicode=getEmojiByUnicode(unicode);

        tvRateInfo.setText(strInfo+smileUnicode);

        tvNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Cancel clicked");
                mHost.onNoThanks(RateusStatus.this);
                dismiss();
            }
        });
        tvRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Cancel clicked");
                mHost.onSure(RateusStatus.this);
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
        mHost = (RateusStatusListener) context;
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
