package com.appuccino.droidpacks.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import com.appuccino.droidpacks.extra.MyLog;
import com.appuccino.droidpacks.listadapters.ListAdapterLibrary;
import com.appuccino.droidpacks.objects.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadAppTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private ProgressDialog dialog;
    private ListAdapterLibrary adapterCallback;
    private App app;
    private final String SUCCESS_CODE = "DOWNLOAD_SUCCESS";
    public final static String FILE_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DroidPacks/temp/";
    public final static String FILE_PATH = FILE_DIRECTORY + "tempapk.apk";

    public DownloadAppTask(Context context, ListAdapterLibrary c, App a, ProgressDialog progressDialog) {
        this.context = context;
        this.dialog = progressDialog;
        this.adapterCallback = c;
        this.app = a;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {
            //before doing anything, make the directory that is to be downloaded to
            File mkdir = new File(FILE_DIRECTORY);
            mkdir.mkdirs();

            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(FILE_PATH);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) { // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                    dialog.setProgress((int) (total * 100 / fileLength));
                }
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return SUCCESS_CODE;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        MyLog.i("SERVER RESPONSE: " + s);

        dialog.dismiss();
        if(!s.equals(SUCCESS_CODE)){
            Toast.makeText(context, "Error downloading app, please try again", Toast.LENGTH_LONG).show();
        } else {
            adapterCallback.installApp(app);
        }
    }
}
