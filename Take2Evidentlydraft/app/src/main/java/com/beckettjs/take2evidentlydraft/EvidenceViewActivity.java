package com.beckettjs.take2evidentlydraft;

import android.app.LauncherActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Recycler;

import java.util.ArrayList;

public class EvidenceViewActivity extends AppCompatActivity {
   protected Recycler evdlist;
   protected LauncherActivity.ListItem evditem;
   ArrayList<EvidenceObject> evdList =new ArrayList<EvidenceObject>();
   protected ArrayList<String> evdStrArr;
   protected RecyclerView visibleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidence_view);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //do some cleanup


        evdStrArr = null;


        Intent intent = getIntent();
        if( intent.hasExtra("evdArr")) {
            //evdStrArr = intent.getStringArrayExtra("evdArr");
           //TODO ttempt to invoke virtual method 'java.util.Iterator java.util.ArrayList.iterator()' on a null object reference
            //        at com.beckettjs.take2evidentlydraft.EvidenceViewActivity.onCreate(EvidenceViewActivity.java:54)
           // if(evdStrArr == null){
                evdStrArr = (ArrayList<String>) intent.getExtras().get("evdArr");
          //  }

            //for each array element, parse URI, create evd object?? , add to evdList
            for (String ele : evdStrArr) {

//                EvidenceObject evdT;
//                ObjectInputStream objectInputStream;
                //clean ele String here
               ele = ele.replace("%20", " ").replace("%3A",":");
                      //  .replaceFirst("/","").replaceFirst("/","");
                Uri uriT = Uri.parse(ele);
                try {
                    //TODO the string uri is pointing to just images (media) and not evidence objects here...
//                    objectInputStream = new ObjectInputStream(new FileInputStream(ele));//filenotfound exception, no such file or directory.....
//                    evdT = (EvidenceObject) objectInputStream.readObject();
                    //do we need to do something with list items adding here?
                   // evdList.add(evdT);
                    evdList.add((new EvidenceObject(uriT)));
//                    evdT = null;
//                    objectInputStream.close();
                } catch (Exception e) {
//                    evdT = null;
                    e.printStackTrace();
                }
            }

            visibleList = findViewById((R.id.directevdrecyclelist));//directevdrecyclelist//evidence_list
            RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(this);
            visibleList.setLayoutManager(layoutManager);
            //we need to atach an adapter??
            //we need to intialize evidence objects from case overview current case evidence list??
            myVisListAdapter myVisListAdapter = new myVisListAdapter(evdList);
            visibleList.setAdapter(myVisListAdapter);

        }
        //fetch case folder from intent
        //navigate to evidence folder
        //extract name and a URI to populate list

    }
    protected void Intenter(Intent intent){
        startActivity(intent);
    }
    protected void itemOnClick(){
        //create intent
        //add uri to intent
        //open intent to view evidence detail using URI

    }

}
