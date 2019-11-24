package com.liker.android.Tool.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.liker.android.R;


public class Network extends DialogFragment {
    private final String TAG = "AUC_SIMPLE";
/*
    private NetworkDialogListener mHost;
    // TODO: Implement an interface for hosts to get callbacks
    public interface NetworkDialogListener{
        public void onPositiveResult(DialogFragment dlg);
    }*/



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //TODO: Create an AlertDialog.Builder instance
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //TODO: Set builder properties
          builder.setTitle(getString(R.string.network_error));
          builder.setIcon(R.drawable.ic_warning_black_24dp);
          builder.setMessage(getString(R.string.your_internet_connection_might_not_be_working_at_the_moment));
          builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  Log.d(TAG,"Positive Button click!");
                //  mHost.onPositiveResult(Network.this);

              }
          });

        // TODO: return the created dialog
        return builder.create();
    }

    // TODO: Listen for cancel message by overriding onCancel

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG,"Dialog cancelled!");
    }


    // TODO: Override onAttach to get Activity instance


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // mHost=(NetworkDialogListener)context;
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
