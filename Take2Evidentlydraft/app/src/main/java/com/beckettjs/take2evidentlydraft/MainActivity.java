package com.beckettjs.take2evidentlydraft;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button fab = findViewById(R.id.button);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               onProceed();
            }
        });
        if(!Settings.canDrawOverlays(this)) {
            requestOverlayPermis();
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            ActivityCompat.requestPermissions((Activity)this,
                    new String[]{Manifest.permission.CAMERA},1);
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);

    }
    protected void onProceed(){
        Intent intent =  new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }
    protected void requestOverlayPermis(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        startActivity(intent);
    }

    }

