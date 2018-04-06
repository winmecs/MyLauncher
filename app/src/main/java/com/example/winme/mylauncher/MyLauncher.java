package com.example.winme.mylauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyLauncher extends AppCompatActivity {

    private MyServer server;

    static public String timezone = null;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startTimeacticity();
            handler.postDelayed(this, 4000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_launcher);

        File index = new File(getFilesDir(),"index.html");
        // First time to start app
        if(!index.exists())
            copyAssets();


        try {
            server = new MyServer(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler.postDelayed(runnable,4000);
    }

    public void startTimeacticity() {
        Intent timeintent = new Intent(this, TimeActivity.class);
            timeintent.putExtra("timezone",timezone);
            startActivity(timeintent);

    }

//   handle the back button
    @Override
    public void onBackPressed(){

    }
    @Override
    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume(){
        super.onResume();
        handler.postDelayed(runnable,4000);
    }
    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getFilesDir(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
