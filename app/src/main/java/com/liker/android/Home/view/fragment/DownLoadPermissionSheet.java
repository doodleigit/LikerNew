package com.liker.android.Home.view.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.liker.android.R;
import com.liker.android.Setting.view.SettingActivity;
import com.liker.android.Tool.CheckForSDCard;
import com.liker.android.Tool.NetworkHelper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;
import static com.facebook.FacebookSdk.getApplicationContext;


public class DownLoadPermissionSheet extends BottomSheetDialogFragment implements
        View.OnClickListener,
        EasyPermissions.PermissionCallbacks {
    // private BottomSheetListener mListener;

    public static final String STRING_URL_key = "string_url_key";
    public static final String VIDEO_FLAG_key = "video_flag_key";
    private boolean networkOk;
    // File url to download
    //  private  String file_url = "http://www.freeimageslive.com/galleries/objects/general/pics/woodenbox0482.jpg";
    private String file_url;
    private boolean isVideoUrl;
    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = DownLoadPermissionSheet.class.getSimpleName();
    private TextView tvMediaDownLoad;

    public static DownLoadPermissionSheet newInstance(String stringUrl,boolean  isVideoUrl) {
        Bundle args = new Bundle();
        args.putString(STRING_URL_key, stringUrl);
        args.putBoolean(VIDEO_FLAG_key,isVideoUrl);
        DownLoadPermissionSheet fragment = new DownLoadPermissionSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkOk = NetworkHelper.hasNetworkAccess(getContext());
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        Bundle argument = getArguments();
        if (argument != null) {
            file_url = argument.getString(STRING_URL_key);
            isVideoUrl = argument.getBoolean(VIDEO_FLAG_key);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.download_permission_sheet, container, false);
        root.findViewById(R.id.downloadContainer).setOnClickListener(this);
        root.findViewById(R.id.newTabContainer).setOnClickListener(this);
        root.findViewById(R.id.copyContainer).setOnClickListener(this);
        tvMediaDownLoad=root.findViewById(R.id.tvMediaDownLoad);
        if(isVideoUrl){
            tvMediaDownLoad.setText(getResources().getText(R.string.download_video));
        }else {
            tvMediaDownLoad.setText(getResources().getText(R.string.download_image));
        }
        return root;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.downloadContainer:
                //Check if SD card is present or not
                if (CheckForSDCard.isSDCardPresent()) {
                    //check if app has permission to write to the external storage.
                    if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Get the URL entered
                        new DownloadFile().execute(file_url);
                    } else {
                        //If permission is not present request for the same.
                        EasyPermissions.requestPermissions(getActivity(), getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "SD Card not found", Toast.LENGTH_LONG).show();
                }
                dismiss();
                break;
            case R.id.newTabContainer:
                webLink("", file_url);
                dismiss();
                break;
            case R.id.copyContainer:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Link", file_url);
                clipboard.setPrimaryClip(clip);
                dismiss();
                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());

    }

    private void webLink(String type, String link) {
        Intent termsIntent = new Intent(getActivity(), SettingActivity.class);
        termsIntent.putExtra("type", type);
        termsIntent.putExtra("link", link);
        getActivity().startActivity(termsIntent);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        //  url = editTextUrl.getText().toString();
        new DownloadFile().execute(file_url);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(
                    getActivity());
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;
                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
                //Create androiddeft folder if it does not exist
                File directory = new File(folder);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    //   Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }


}

