package com.beckettjs.take2evidentlydraft;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Executors;


//https://www.simplifiedcoding.net/android-floating-widget-tutorial/

public class widgetActivity extends Service implements View.OnClickListener,  LifecycleOwner {

    private LifecycleRegistry lifeCycleRegistery;
    private WindowManager winMan;
    private View floaterView;
    private View collapserView;
    private View ExpanderView;
    Button closerButt;
     Uri caseUri;
    String currentPhotoPath;
    Uri currentVideoUri;
    CaseObject currentCase;
    public widgetActivity() {
        Integer ints = 2 + 2;


    }

    public  Lifecycle getLifecycle(){
    //cannot return null here??
        return lifeCycleRegistery;
    }
    @Nullable
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public IBinder onBind(Intent intent) {
        caseUri = Uri.parse(intent.getStringExtra("URI"));

        return null;
    }


    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {

        super.onCreate();

        lifeCycleRegistery = new LifecycleRegistry(this);
        lifeCycleRegistery.markState(Lifecycle.State.STARTED);
        //fetch layout
        floaterView = LayoutInflater.from(this).inflate(R.layout.widget, null);

        //param layout
        final WindowManager.LayoutParams layParam = new
                WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        //get a window serv and += float view
        winMan = (WindowManager) getSystemService(WINDOW_SERVICE);
        winMan.addView(floaterView, layParam);
        //grab big and smal layout
        collapserView = floaterView.findViewById(R.id.layoutCollapser);
        ExpanderView = floaterView.findViewById((R.id.layoutExpandee));
        //we wann let this be draggable
        floaterView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(
                new View.OnTouchListener() {
                    private int inity;
                    private int initx;
                    private float initToucx;
                    private float initToucy;

                    @Override
                    public boolean onTouch(View v, MotionEvent movent) {
                        switch (movent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                initx = layParam.x;
                                inity = layParam.y;
                                initToucx = movent.getRawX();
                                initToucy = movent.getRawY();
                                return true;
                            //lets just put a button on it
                            // case MotionEvent.ACTION_UP:
                            //    collapserView.setVisibility(View.GONE);
                            //    ExpanderView.setVisibility(View.VISIBLE);
                            //    return true;
                            case MotionEvent.ACTION_MOVE:
                                layParam.x = initx + (int) (movent.getRawX() - initToucx);
                                layParam.y = inity + (int) (movent.getRawY() - initToucy);
                                winMan.updateViewLayout(floaterView, layParam);
                                return true;

                        }
                        return false;
                    }

                }


        );
        //SO THIS DOESNT BRING IT BACK ON SECOND OPENING....
        if (floaterView == null) winMan.addView(floaterView, layParam);


        //we need to set onclicklisterners fro butans
        //   closerButt = floaterView.findViewById(R.id.buttonClose);
        //closerButt.setOnClickListener(new View.OnClickListener() {

        //     @Override
        //     public void onClick(View view) {
        //        onDestroy();
        //    }
        // });
        File file = new File(getFilesDir(), "lastURI.las");
        //"lastURI.las"
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();

            caseUri = Uri.parse(text.toString().replace("%20"," "));
            currentCase = new CaseObject(caseUri);
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

    }


    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (floaterView != null) winMan.removeView(floaterView);
        super.onDestroy();

    }

    //here is where we deal with all our clicks on widget??
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpandee:
                collapserView.setVisibility(View.VISIBLE);
                ExpanderView.setVisibility(View.GONE);
                break;
            case R.id.buttonClose:
                onDestroy();
                break;
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void killMe(View v) {
        onDestroy();
    }

    public void expandMe(View v) {
        collapserView.setVisibility(View.GONE);
        ExpanderView.setVisibility(View.VISIBLE);
    }

    public void enSmallenMe(View v) {
        collapserView.setVisibility(View.VISIBLE);
        ExpanderView.setVisibility(View.GONE);
    }

    public void screenshotTry(View v) {
        // hide overlay?
        // screenshot keycode
        Process proc;
        try {

            String sdcard = Environment.getExternalStorageDirectory().toString();
            File dir = new File((sdcard + "/Pictures/Screenshots")); //default scrrenshot storae loc
            dir.mkdirs();
            dir.setReadable(true, false);
            File[] list = dir.listFiles();
//                so what if we just grab the last screenshot instead of rying to take one
            // as we cant just take a scrnshot due to hardbaked permissions
            Arrays.sort(list, new Comparator<File>() {

                @Override
                public int compare(File o1, File o2) {
                    return Long.valueOf(o1.lastModified()).compareTo(o2.lastModified());
                }
            });

            //get most recent file in screenshots, should be last one or...first one....
            File scrnshotx = list[0];
            File scrnshoty = list[list.length - 1]; //if i remember java counting correctly

            //get case
            CaseObject caser = new CaseObject(caseUri);
            //create new evidenvce in case
            caser.addtoEvidenceArr(Uri.parse(scrnshoty.toURI().toString()));
            caser.saveToUri(caseUri.toString());

            //

        } catch (Exception e) {
        }
        //bringback overlay?

    }

    public void cameraTry(final View v) {

        TextureView textureView =((View) v.getParent()).findViewById(R.id.texture_cam);
        startCamera(textureView);

    }
    private void startCamera(TextureView textureView){
        CameraX.unbindAll();
        final ViewGroup paento  = (ViewGroup) textureView.getParent();
        //Rational aspect = new Rational(textureView.getWidth(),textureView.getHeight());
        Size    scrn = new Size(textureView.getWidth(), textureView.getHeight());
        PreviewConfig previewConfig = new  PreviewConfig.Builder().setTargetResolution(scrn).build(); //.setTargetAspectRatio(AspectRatio.RATIO_4_3).
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    @Override
                    public void onUpdated(@NonNull Preview.PreviewOutput output) {

                        paento.removeView(textureView);
                        paento.addView(textureView, 0);
                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                    }
                }
        );

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(winMan.getDefaultDisplay().getRotation()).build();
    final ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);
            textureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(getDataDir() + "/Pictures");
                    file.mkdirs();
                    file = new File(file, String.valueOf(new Date())+".jpg");
                    //TODO a-makea dah file here.
                    if(!file.exists()){try {
                        file.createNewFile();
                    }catch (Exception e){
                        int chkserrr = 1 + 2;
                    };}
                    imageCapture.takePicture(file, Executors.newSingleThreadExecutor(), new ImageCapture.OnImageSavedListener() {
                        @Override
                        public void onImageSaved(@NonNull File file) {
                            String msg = "Pic captured at " + file.getAbsolutePath();
                            //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//                            /add to case evidences
                            currentCase.addtoEvidenceArr(Uri.fromFile(file));
                            currentCase.saveToUri(currentCase.getCaseUri().toString());
                            //create and save an eviddence object
                            EvidenceObject obj = new EvidenceObject(file);
                        }

                        @Override
                        public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            String msg = "Pic capture failed : " + message;
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                            if (cause != null) {
                                cause.printStackTrace();
                            }
                        }
                    });
                    //TODO prolly make the switch back to widget buttons ofter taking picaroonies.
                    paento.removeView(textureView);
                }
            });

    CameraX.bindToLifecycle((LifecycleOwner)this, preview, imageCapture);
    }

    private java.lang.Runnable updateTransform(){ return null;}

    public  void videoTry(View v){
    Intent vidyaIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    //provide save location?? //no


    if(vidyaIntent.resolveActivity(getPackageManager()) != null) { //chcek for camera
            shellActivity shell = new shellActivity();
            shell.shellCamera(vidyaIntent, this);
            Uri viduri = shell.getVidUri();
            //do something with vidUri
        currentVideoUri = viduri;
        }

    }
    public void backToCaseButt(View view){
//            fetch origianl intent and backtrack??

        Intent intent = new Intent(this, CaseOverviewActivity.class);
        intent.putExtra("URI", currentCase.getCaseUri());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.stopSelf();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = (new Date()).toString().replaceAll("[ \\ /]", "");
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File( storageDir, imageFileName+".jpg");
        ;

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    }
    //subclass to handle start actiivty for result
    class  shellActivity extends Activity {
        static final int REQUEST_IMAGE_CAPTURE = 1;
        Bitmap imgCam;
        Uri vidUri;

    public void shellCamera(Intent intent, Context context){
        //Bitmap bitt;
        //attremp to invoke virtual method........
         int REQUEST_IMAGE_CAPTURE = 1;


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
//        this.attachBaseContext(context);
//        startActivityForResult(intent ,1);


    }
    public void requestCamPermiss(Context context){ //have to do this ealier eh
        ActivityCompat.requestPermissions((Activity) context ,
                new String[]{Manifest.permission.CAMERA},1);
    }
    @Override
    public void  onActivityResult( int reqc, int resc, Intent dat){
        if(reqc == REQUEST_IMAGE_CAPTURE && resc == RESULT_OK){
            Bundle extras = dat.getExtras();
            try { // is it an img ?
                imgCam = (Bitmap) extras.get("data");
            }catch (Exception e){
                try{ // is it a video??
                    vidUri = dat.getData();
                }catch (Exception er){}
            }
        }
    }
    public Uri getVidUri(){return vidUri;}
    public Bitmap getImgCam(){return imgCam;}

}



/*         // FileOutputStream fos = new FileOutputStream(dir);
            proc = Runtime.getRuntime().exec("input keyevent "+KEYCODE_SYSRQ);
            //focus window here!!
            //get view window
            Integer var = view.getRootView().getId();//.dispatchWindowFocusChanged(true)   ;//        .requestFocus().;
            view.setFocusable(true);
            view.requestFocus();


                        //view.requestFocus();
            view.focus

            BaseInputConnection mInputConnection = new BaseInputConnection(view.getRootView(), true);
            mInputConnection.sendKeyEvent(new KeyEvent(0,KEYCODE_SYSRQ));
            //event dropped due to no focus window??
            //  fos.close();
        }catch (Exception e){
            //handle
            String err = e.getStackTrace().toString();
            e.printStackTrace();*/