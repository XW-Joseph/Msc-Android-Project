package com.beckettjs.take2evidentlydraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

public class SelectionActivity extends AppCompatActivity {
Uri uriLast ;
Uri caseFolderUri;
String lastUripathname;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    File[] caseDirArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Button SnapNew = findViewById(R.id.buttonOpnew);
        SnapNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onOpenNew();
            }

            //fetch Last opened case URI from locationX
            //uriLast = that


        });
        Button SnapLast = findViewById(R.id.buttonOpLas);
        SnapLast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onOpenLast();
            }
        });
        Button DetNew = findViewById(R.id.buttondetNew);
        DetNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onDetailNew();
            }
        });
        Button detLast = findViewById(R.id.buttonDetLast);
        detLast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onDetailsLast();
            }
        });
        Button onSearchReq = findViewById(R.id.buttonSearch);
        onSearchReq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onSearchAll();
            }
        });
       //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // here we set up the last opened URI
        uriSetup();
        //find or create case folder
        caseFolderSetup();
        //or disable buttons if no last opens??
    }
    protected void caseFolderSetup(){
       // File file = Environment.getDataDirectory();
        File file2 = getFilesDir();
        //if no cases folder make folder and return
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name == "Cases") {
                    return true;
                } else {

                    return false;
                }
            }
        };
        //here we fail to make the directory....reason permission? wrong method used? path looks okay
        //File tryFile = new File(file.getAbsolutePath()+System.getProperty("file.separator")+"Cases");
        File tryFile2 = new File(file2.getAbsolutePath()+System.getProperty("file.separator")+"Cases");
       // tryFile.mkdirs();
        tryFile2.mkdirs();
        tryFile2.exists();
      //  caseDirArr = file.listFiles(filter);
        //if no cases folder make folder and return
        //try{ File bo = caseDirArr[0];}catch ( Exception e){
          //      new File(file.getAbsolutePath()+System.getProperty("file.separator")+"Cases").mkdir();
          //      caseDirArr = file.listFiles(filter);
          //  }
        File caseDir =tryFile2;// caseDirArr[0];
        String casePath = caseDir.getPath();
        caseFolderUri =  Uri.parse(casePath);

    }
    protected void uriSetup(){
        try {
            File file = getFilesDir();
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name == "lastURI.las") {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            File datadir = Environment.getDataDirectory();
            File file2 = new File( datadir, "/user/0/com.beckettjs.take2evidentlydraft/files");
            file2.mkdirs();
            file2 = new File(file2, "lastURI.las");
            //ftch contents f file2

            lastUripathname = file2.getPath();


            FileInputStream fint = new FileInputStream(file2);
                int len = (int) file2.length();
                byte[] bite = new byte[len];
                try {
                    fint.read(bite);
                }finally {
                    fint.close();
                }
                uriLast = Uri.parse(new String(bite));


        } catch (Exception e) {
            File file = getFilesDir();
            String rootPath = file.getPath();
            String fileSep = System.getProperty("file.separator");
            if(fileSep == null){fileSep = "/";}
            String nuUrilastpath = rootPath+fileSep+"lastURI.las";
            File fileNu = new File(nuUrilastpath);
            String uritext = fileNu.toString();
            uriLast = Uri.parse(uritext);
            e.printStackTrace();
        }


    }
    protected void onOpenNew(){
//        snap a widget leading to a new case
       CaseObject casey = new CaseObject();
       Uri caseyUri = Uri.parse(casey.caseUri);
        Intent intent = new Intent(this, widgetActivity.class);
       // intent.putExtra("CaseUri",  caseyUri); //new case objects generated on intent other side??

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
    protected void onOpenLast(){
//        snap a widgget leading to last opened case;
        Uri caselast = fetchUrifromfile(lastUripathname);
        //needed?
        //CaseObject  caseObject = new CaseObject(caselast);
        Intent intent = new Intent(this, widgetActivity.class);
        intent.putExtra("CaseUri",  caselast);

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
    protected Uri fetchUrifromfile(String filename){
//        String conts = "";
//        try {

//            //this oesnt work
//            conts = new String( Files.readAllBytes(Paths.get(filename)) );
//        }catch(Exception e){
//
//
//        }
        uriLast = null;
        try {
            File nu = new File(filename);
            if (!nu.exists()) {
                try {
                    nu.createNewFile();
                } catch (Exception e) {
                    int chrek = 1 + 1;
                }
            }
            FileInputStream fint = new FileInputStream(Uri.parse(nu.getAbsolutePath()).toString());
            int len = (int) nu.length();
            byte[] bite = new byte[len];
            try {
                fint.read(bite);
            } finally {
                fint.close();
            }
            uriLast = Uri.parse(new String(bite));
        }catch (Exception e){
            e.printStackTrace();
        }
        return uriLast;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onDetailNew(){




        //use new case and go to case screen for reasons
        Intent intent = new Intent(this, CaseOverviewActivity.class);
        //intent.putExtra("URI", "null");


        startActivity(intent);
    }
    protected void onDetailsLast(){
        //get last opened case

        //use last case to open case screen for fillage
        Intent intent = new Intent(this, CaseOverviewActivity.class);
       //uriLast is pointing to lasturi.las.. sometimes

        uriLast = fetchUrifromfile(lastUripathname);




        //
        if( uriLast != null||uriLast !=  Uri.parse("null")||uriLast.toString() != ""){  //may work....
            intent.putExtra("URI",uriLast.toString());
        }else{ }
        startActivity(intent);
    }

    protected void onSearchAll(){
        Intent intent = new Intent(this, CaseObjectListActivity.class);
        startActivity(intent);
    }

}
