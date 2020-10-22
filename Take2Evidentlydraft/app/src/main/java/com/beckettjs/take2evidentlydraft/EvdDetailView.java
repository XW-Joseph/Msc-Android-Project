package com.beckettjs.take2evidentlydraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class EvdDetailView extends AppCompatActivity {
EvidenceObject evdobj;
EditText evdTitle;
EditText createDate;
EditText modifDate;
Button openMedia;
EditText evdStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evd_detail_view);
        //fetch uri from intent
        if( getIntent().hasExtra("evdUri")){
            String UriStr = getIntent().getExtras().getString("evdUri"); //TODO  is null ??????
            evdobj = new EvidenceObject(Uri.parse(UriStr));
            evdTitle = findViewById(R.id.EvdTitle);
            createDate = findViewById(R.id.evdCreateDate);
            modifDate = findViewById(R.id.evdModifDate);
            openMedia = findViewById(R.id.openMedia);
            evdStory = findViewById(R.id.evdStory);

            evdTitle.setText(evdobj.getTitle());
            evdStory.setText((evdobj.getDetail()));
            createDate.setText(evdobj.getDateCreate().toString());
            if(evdobj.getDateModif()!= null) {
                modifDate.setText(evdobj.getDateModif().toString());
            }
            openMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toMedia();
                }
            });


        }
    }
    private void toMedia(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //so we cant expose the uri of an app internal file over an intent to  the outside
        //so.. we saveit in external somewhere and pass that??
        File file = new File(evdobj.getMediaUriAsString().replace("file:",""));
        File file2 = new File(getExternalMediaDirs()[0],"/tmpEvd.jpg");

        try {
            FileInputStream fis = new FileInputStream(file);

            FileOutputStream fos = new FileOutputStream(file2);
            fos.write(fis.read());
            fis.close();
            fos.close();
        }catch (Exception e){e.printStackTrace();}
        intent.setDataAndType(Uri.parse(file2.getAbsolutePath()), "image/*");

            startActivity(intent);


    }
    public  void onDestroy(){
        super.onDestroy();
        try {
            FileOutputStream fos = new FileOutputStream(new File(evdobj.getObjectUriAsString()));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            evdobj.setDetail(evdStory.getText().toString());
            evdobj.setTitle(evdTitle.getText().toString());
            evdobj.setDateModif(new Date());
            oos.writeObject(evdobj);
            oos.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
