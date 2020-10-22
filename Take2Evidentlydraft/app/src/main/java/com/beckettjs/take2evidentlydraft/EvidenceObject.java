package com.beckettjs.take2evidentlydraft;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;


class EvidenceObject implements Serializable {
private String detail;
private  String title;
private String mediaUriAsString;
private Integer selector;
private Date createDate;
private static Date createSDate;
private Date modifDate;
private  String mediaTypeString;
private  String ObjectUriAsString;
private String EvidenceLog;

    public void onCreate(Uri uri){
        //fill data from file
        //required deterimin what media type is present
    }
    public EvidenceObject(){
        //be sure to set up  tthe object uri if nothing else you moron
        //something something evidences folder soething date something
        File nuF = new File(Environment.getDataDirectory(),"user/0/com.beckettjs.take2evidentlydraft/files/Evidences/"+new Date().toString());
        ObjectUriAsString = Uri.fromFile(nuF).toString().replace("%20"," ").replace("%3A",":");
//        atadir = Environment.getDataDirectory();
//        File file2 = new File( datadir, files
        addToEvidenceLog("intitial object file space reseeve");
    }
    public  EvidenceObject(Uri uri){ // constructor from existing saved object
        EvidenceObject retrunObject;
        ObjectInputStream objectInputStream;
        uri = Uri.parse(uri.toString().replace("%20"," "));
        String[] strings = uri.toString().split("/");
        //this may need tweaking indexes future  joe
        File Dir = new File(strings[1]+"/"+strings[2]+"/"+strings[3]+"/"+strings[4]+"/"+strings[5]+"/"+strings[6]);

        File filer = new File(Dir, strings[7]);
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(filer));
            retrunObject = (EvidenceObject) objectInputStream.readObject();
            setDetail(retrunObject.getDetail());
            setTitle(retrunObject.getTitle());
            setDateModif(retrunObject.getDateModif());
            createDate = retrunObject.getDateCreate();
            setMediaUriAsString(retrunObject.getMediaUriAsString());
            setObjectUriAsString(retrunObject.getObjectUriAsString());
            if( getObjectUriAsString() == null){
                setObjectUriAsString(uri.toString());
            }
            setEvidenceLog(retrunObject.getEvidenceLog());

        }catch (Exception e){

        }
        addToEvidenceLog("constructed from existing saved object");
    }
    protected void setMediaUriAsString(String str){ mediaUriAsString = str;}
    protected void setObjectUriAsString(String str){ObjectUriAsString = str;}
    protected void setEvidenceLog(String str){ EvidenceLog = str;}
    protected String getEvidenceLog(){ return EvidenceLog;}
    private void addToEvidenceLog(String str){
        EvidenceLog = EvidenceLog+"\n"+new Date()+":" + str;
    }
    public EvidenceObject(File file){
        //constuctor from new media object
        //from file direct
//
         String uri = Uri.fromFile(file).toString().replace("%20"," ").replace("%3A",":");
         //TODO we're just saving where it is here now....
        mediaUriAsString = uri;
        createDate = new Date();
        createSDate = createDate;
        setTitle(createDate.toString());
        setDetail("description");

        mediaTypeString = determineMediaType(file);
        ObjectUriAsString =createNewEvidencefile();


//        String mimeType;
//        ContentResolver resolver;
//           resolver = ((Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null)).getContentResolver();
 addToEvidenceLog("created from  file");
               //
    }
    protected static EvidenceObject fromMedia(Uri uri){
        EvidenceObject retrunObj = new EvidenceObject();

        retrunObj.setMediaUri( uri );
        //TODO create new    File to save object to, call save(uri(ofFile)) , retrun this retrun object
        File Ddir = Environment.getDataDirectory();
        File Dir = new File(Ddir,"user/0/com.beckettjs.take2evidentlydraft/files/Evidences" );
        Dir.mkdirs();
        Date data;
        if(getSDateCreate()!= null){
            data = getSDateCreate();
        }else { data = new Date();}
        Dir.mkdirs();
        String dats = data.toString();
        File Efile = new File(Dir, dats);
        try {
            //Efile.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        //GET  FILE URI, CALL SAVE(URI) AND CLOSE
        Uri fUri = Uri.fromFile(Efile);

        retrunObj.saveToUri(Uri.parse(retrunObj.getObjectUriAsString().replace("%20"," ").replace("%3A",":").replace("file://","")));


        return retrunObj;

    }
    private String determineMediaType(File file){return " not implemented";}
    private void setMediaUri(Uri uri){ mediaUriAsString = uri.toString();}
    private String createNewEvidencefile(){
        File evidencesDir = new File(Environment.getDataDirectory(), "user/0/com.beckettjs.take2evidentlydraft/files/Evidences");
        evidencesDir.mkdirs();//in case of first time access
        File evdFile = new File(evidencesDir, new Date().toString());
        try {
            //evdFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(evdFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
                oos.close();
                fos.close();
        }catch (Exception e){e.printStackTrace(); } //probably a %20 or similar error
        addToEvidenceLog("created new file for object");

        return evdFile.toString();//should be file name here
        }

    protected void save(){
        //save parts to dir at Uri
    }
    protected void load(){

    }
    protected boolean saveToUri(Uri uri){
        boolean bool = false;
        //this.objectoutputstrem into file @ uri
        String cleanUri = uri.toString();
        cleanUri.replace("%20"," ").replace("%3A",":");
        File file = new File(cleanUri);
        ObjectUriAsString = cleanUri;
        //file.delete();
        //notify modif date
        this.setDateModif(new Date());
        try {
            //file.createNewFile();
            FileOutputStream fis = new FileOutputStream(file);
            ObjectOutputStream ois = new ObjectOutputStream(fis);
            ois.writeObject(this);
            ois.close();
            fis.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        addToEvidenceLog("saved");
        return bool;

    }
    public void onDestroy(){
        this.saveToUri(Uri.parse(ObjectUriAsString));
    }
    protected String getDetail(){return detail;}
    protected String getMediaUriAsString(){return mediaUriAsString;}
    protected String getObjectUriAsString(){return ObjectUriAsString;}
//    protected MediaStore.Video.Media getVideoMedia(){return videoMeadia;}
//    protected MediaStore.Audio.Media getAudioMedia(){return audioMedia;}
//    protected MediaStore.Images.Media getImageMedia(){return imageMedia;}
    protected String getTitle(){return title;}
    protected  Date getDateCreate(){return this.createDate;}
    protected Date getDateModif(){return modifDate;}
    protected static Date getSDateCreate(){return createSDate;}
    protected void  setDetail(String detStr){detail = detStr; addToEvidenceLog("detail updated");}
//    protected void setVideoMedia(MediaStore.Video.Media vidya){videoMeadia = vidya;}
//    protected void setAudioMedia(MediaStore.Audio.Media sound){audioMedia = sound;}
//    protected void setImageMedia(MediaStore.Images.Media Image){imageMedia = Image;}
    protected void setTitle(String titStr){title = titStr; addToEvidenceLog("title updated");}
    //protected void setDateCreate(){} immutable after creation
    protected void setDateModif(Date nuDate){modifDate = nuDate;}
    private Integer getSelector(){
        if(selector != null) {
            return selector;
        }else{return 0;}
    }
    protected File createExportableFile(){
        //make-a de folder
        File Ddir = Environment.getDataDirectory();
        File Dir = new File(Ddir, "tempEvd"+getTitle());
        Dir.mkdirs();
        addToEvidenceLog("called to export");
        // with text file title, dates and descrip
        File txtFile = new File(Dir, "Texts_"+getTitle()+".txt");
        try{ txtFile.createNewFile();
            FileWriter writer = new FileWriter(txtFile);
            writer.write(getTitle() + "\n");
            writer.write(getDetail()+ "\n");
            writer.write("Create Date"+ "\t");
            writer.write( getDateCreate()+ "\n" );
            writer.write("Last Modified"+ "\t");
            writer.write(getDateModif() + "\n");
            writer.write( getEvidenceLog() +"\n");
            writer.close();


        }catch (Exception e){
            e.printStackTrace();
        }
        File medFile = new File(Dir, "media_");
        try{
            medFile.createNewFile();
            FileInputStream fis = new FileInputStream((new File(mediaUriAsString)));
            FileOutputStream fos = new FileOutputStream(medFile);
            fos.write(fis.read());
            fis.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Dir;
        //                     other file for media..
    }
    protected PdfDocument.Page createExportablePdfPage(){
        //TODO here now we are getting to grips with akeing a pdf page for export
        PdfDocument pdf = new PdfDocument();
        PageInfo pageInfo = new PageInfo.Builder(100,100,1).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        addToEvidenceLog("called to genarate PDF");
        String texts = getTitle() + getDateCreate() + getDetail() + getDateModif()+ getEvidenceLog();
        canvas.drawText(texts,10, 5, new Paint());
        //draw image ??
        try {
            //FileInputStream fis = (new FileInputStream(new File(mediaUriAsString)));
           Bitmap bit =  BitmapFactory.decodeFile(mediaUriAsString);
            canvas.drawBitmap( bit, 0, 30, new Paint());
//            canvas.dr
        }catch (Exception e){
            e.printStackTrace();
        }


        pdf.finishPage(page);
        return page;
    }

}
