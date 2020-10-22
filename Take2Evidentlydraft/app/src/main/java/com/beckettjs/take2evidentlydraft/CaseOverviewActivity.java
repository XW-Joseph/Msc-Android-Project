package com.beckettjs.take2evidentlydraft;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;


public class CaseOverviewActivity extends AppCompatActivity {
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    Button toEvd;
    FloatingActionButton addEvd;
    TextInputLayout titleText;
    EditText titleTextInput;
    EditText dateCreate;
    EditText dateModif;
    TextInputLayout caseStory;
    TextInputEditText caseEditText;
    Button fileMakeButt;
    CaseObject currentCase;
    Uri uriToOpen;
    Uri currentCaseUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.case_overview);
        toEvd     = findViewById(R.id.toEvidenceButton);
        toEvd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goToEvidenceList();
            }
        });

        //tie other secreen fields to handles here
         titleText = findViewById(R.id.caseTitleText);
         titleTextInput = findViewById((R.id.titleInputEdit));

        dateCreate =findViewById(R.id.evdCreateDate);
         dateModif  = findViewById(R.id.modifDate);
         //wrong container??
         caseStory = findViewById(R.id.textInputLayout);
         caseEditText = findViewById(R.id.caseEditText);
        addEvd = findViewById(R.id.addToCaseEvidence);
        addEvd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddEvidence();
            }
        });
        fileMakeButt = findViewById(R.id.fileMakeingButt);
        fileMakeButt.setOnClickListener(new View.OnClickListener(){
            public void onClick( View v){ makeaDeFile(v);}
        });

        //here we fetch the URI from selection activity to load case data
        //if null, new initial case
        //check for extras getIntent().hasExtra("URI") &&
        //TODO: sort this out, i think from the previous screen, although we are no saving lastURI file correctly
        // we seem to not be passing anything here.
        if((getIntent().hasExtra("URI")) ==  true &&
                ((getIntent().getStringExtra("URI"))) != "")  {

        String tString ;
                tString =  (getIntent().getStringExtra("URI")) ;
        if(tString == null){
       Uri tUri = (Uri)getIntent().getExtras().get("URI");
       uriToOpen = tUri;
        }else {
//            uriToOpen = Uri.parse(getIntent().getStringExtra("URI"));
            uriToOpen = Uri.parse(tString);
        }
// i think we need to replace % with [SPACE] in uritoopen here
                 currentCase = new CaseObject(uriToOpen);
                // thetch data to fill screen elements them from caseobject
                titleText.getEditText().setText(currentCase.caseTitle);
                dateCreate.setText(currentCase.createDate.toString());

                //account for cas not bing fully set up if(x exists){fillout field}

                if (currentCase.modifDate != null) {
                    dateModif.setText(currentCase.modifDate.toString());
                    //so here we have the wrong container... i want to set an existing text, then also be able to add to/edit it....+
                    // caseStory;//Text(currentCase.caseStory);

                }

                //disable goto evd when no evd present, when is empty true, enabled false
                Boolean bool = false;
                if (currentCase.evdidencesArr != null) {
                    bool = true;
                }
                toEvd.setEnabled(bool);
                //handle trying to open last case as first case ?????
            //this doesnt proc when true??
                if(currentCase.getCaseUri().toString().length() == 0 ){ //|| currentCase.getCaseUri() == null
                    currentCase = new CaseObject();
                    currentCaseUri = currentCase.getCaseUri();
                    dateCreate.setText( new Date().toString());
                    toEvd.setEnabled(bool);
                }
           // }
            } //else we are opening a new case and need to remember that for opening
        else{//somestuff
            //
             currentCase = new CaseObject();
            currentCaseUri = currentCase.getCaseUri();
            toEvd.setEnabled(false);

            dateCreate.setText( new Date().toString());

            }
        ActivityCompat.requestPermissions(this ,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        //so now we need to set texts as saved values
        caseEditText.setText(currentCase.caseStory, TextView.BufferType.EDITABLE);
        titleTextInput.setText(currentCase.caseTitle,TextView.BufferType.EDITABLE);
        dateModif.setText(new Date().toString());
    }
    //fetch uri from lastUri.las contents mostly
    protected Uri fetchUrifromfile(Uri filepoint){
        String conts = "";
        try {

             conts = new String( Files.readAllBytes(Paths.get(String.valueOf(filepoint))) );
        }catch(Exception e){

            Uri wefwf = this.uriToOpen;
        }
        return Uri.parse(conts);
    }
    protected void goToEvidenceList() {
        //set intent to open evidence view activity
        Intent intent = new Intent(CaseOverviewActivity.this, EvidenceViewActivity.class);
        //fetch array of evd uri/evdobjects fom currentcase
        try {
            ArrayList<String> evdObjecturis = new ArrayList<String>();
            ArrayList<String> other;
            //TODO this evdobjarr seems to be empty??
                    for(EvidenceObject ele:currentCase.evdObjArr){
                        evdObjecturis.add(ele.getObjectUriAsString());
                    }

            intent.putExtra("evdArr", evdObjecturis);
        } catch (Exception e) {
            //do not add extra
            e.printStackTrace();
        }
        //add to intent
        if (currentCase.getEvdidencesArr() != null &&
                currentCase.getEvdidencesArr().get(0) != null){
            startActivity(intent);
    }else{
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText("No Evidences");
        toast.show();
    }
        //note crashes on use with no actual evidence
    }
    protected void addEvidence(EvidenceObject evdobj){}
    protected  void goToAddEvidence(){
        Intent intent = new Intent(this, widgetActivity.class);
            intent.putExtra("CaseUri",  uriToOpen);

        if ( !Settings.canDrawOverlays(this)) {
            askPermission();
        }

        //startService(new Intent(CaseOverviewActivity.this, widgetActivity.class));
        startService(intent);
        //can we jsump to start? should we???
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }
    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    //we need to save case to filesystem on leave/destroy/exit
    //also update last opened case
    protected  void onDestroy(){
//        current case is null here ( def on new case, perhaps on already case also, how do we save gahnges if cant use this...)
      //  currentCase.setCaseTitle(titleText.toString());
        //currentCase.setCaseTitle(titleTextInput.getText().toString());
        currentCase.setCaseStory(caseStory.getEditText().getText().toString());
        currentCase.setCaseTitle(titleText.getEditText().getText().toString());
      //  currentCase.caseStory = caseStory.toString();
       // currentCase.caseStory = caseEditText.getText().toString();

        currentCase.saveToUri(currentCase.getCaseUri().toString());
        currentCase.onDestroy();

        //get lasturi file, delete old contents, update with current case uri

//        saave curret contents of title, story , update modif date with current

        super.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void makeaDeFile(View view){
        File exportable = currentCase.exportToFile(view);
        Boolean bool = exportable.exists();
        //request read write external permis
        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
        }
       try {
//           File[] f = ContextCompat.getExternalFilesDirs(getApplicationContext(), null);//new File(Environment.getExternalStorageDirectory().toURI().toString().replace("file:","")+"/user/0/com.beckettjs.take2evidentlydraft/files/temp"+new Date());
           //File fi = f[0];
           File fil = new File(getExternalStorageDirectory(), "/caseExports");
//           fi.mkdirs();
//           copyDir(exportable,fi);
           FileInputStream fis = new FileInputStream(exportable);
           FileOutputStream fos = new FileOutputStream(fil);
           fos.write(fis.read());
           fis.close();
           fos.close();
//         how();
       }catch (Exception e){e.printStackTrace();}
        //Files.m
    }
    protected void copyDir(File src, File dst)throws IOException {
        File[] filearr = src.listFiles();
        if(filearr != null && filearr.length > 0){
            for(File file:filearr){
                File DirN = new File(dst, file.getName());
                    if(file.isDirectory()&&!DirN.isDirectory()) {

                        DirN.mkdir();
                        copyDir(file, DirN);
                    }if(DirN.isDirectory()&&!file.isDirectory()) {
                    DirN = new File(DirN.getName(), "target" + file.getName().replaceAll("*/", ""));
                    copyDir(file, DirN);
                }if(!file.isDirectory()&&!DirN.isDirectory()){
                    File FileD = new File(dst, file.getName());
                    FileChannel in = new FileInputStream(file).getChannel();
                    FileChannel out = new FileOutputStream(FileD).getChannel();

                    in.transferTo(0,in.size(),out);
                    in.close();out.close();

                }
            }
        }
    }


}