package com.beckettjs.take2evidentlydraft;

import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;

class CaseObject implements Serializable {
    String caseTitle ;
    Date createDate ;
    Date modifDate;
    ArrayList<String> evdidencesArr ;
    ArrayList<EvidenceObject> evdObjArr;
    File caseFile = null;
    String filterString ;
    String caseStory ;
    String caseUri;
    PdfDocument casePdf;
    String caseLog;

    CaseObject(){
        //set up and reserve a uri for a new case objct
        File datadir = Environment.getDataDirectory();
        Date date = new Date();
        this.createDate = date;
        this.caseTitle = date.toString();
        File file2 = new File( datadir, "/user/0/com.beckettjs.take2evidentlydraft/files/Cases/"+date);
        try {
            FileOutputStream fout = new FileOutputStream(file2.getPath());
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            oout.writeObject(this);
            addToCaseLog(new Date() + ":initial creation");
        }catch(Exception e){}
        caseUri = (file2.toURI().toString());
        File datadirw = Environment.getDataDirectory();

        File file = new File( datadirw, "/user/0/com.beckettjs.take2evidentlydraft/files/lastURI.las");
        addToCaseLog( "updated last used file");
        try {
            //clear contents
            file.delete();
            //set contents as caseUri.toString()
            file.createNewFile();
//            this fails
            file.setWritable(true);
            //FileWriter fw = new FileWriter(file);
            FileOutputStream fw = new FileOutputStream(file);
            fw.write(caseUri.toString().getBytes());
            fw.close();
        }catch (Exception e){
            //DEBBUG HOOK
            Integer inter = 2 + 2;
        }
//        does this  also fail /???
        this.saveToUri(caseUri);
        addToCaseLog("saved to file ");
    }
    CaseObject(Uri uri) {

    //det up filter
       final  FilenameFilter filterFile = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {

                if (name == filterString) {
                    return true;
                } else {
                    return false;
                }

            }

        };
         loadFromUri(uri);
         //if new, set create date
        if(createDate == null){createDate = new Date();};


        //set as last uri opened
        File datadir = Environment.getDataDirectory();

        File file = new File( datadir, "/user/0/com.beckettjs.take2evidentlydraft/files/lastURI.las");

        try {
            //clear contents
            file.delete();
            //set contents as caseUri.toString()
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(caseUri.toString());
            fw.close();
        }catch (Exception e){

        }
        //calling thsi overwitts the save with mostly null liol
        //this.saveToUri(uri.toString());
        //
        //i thin kwe overwrith case uri at somepiont here ish...
        this.caseUri = uri.toString();
    }
    protected void setCaseTitle(String title){
        caseTitle = title;
        addToCaseLog( new Date()+ ":title set -"+title);
    }
    protected void setCaseStory(String story){
        caseStory = story;
        addToCaseLog(new Date()+ ":Story set");
    }
    protected void setModifDate(){
        modifDate = new Date();
        addToCaseLog(new Date()+ ":modified at");
    }
    protected void setCaseFile(){} // this may be superfluous
    protected void addtoEvidenceArr(Uri uri){

//         clean uri

        if( evdidencesArr== null || evdidencesArr.size()==0 ){
            evdidencesArr = new ArrayList<String>();
        }
        evdidencesArr.add(uri.toString());
        //clean uri
        uri = Uri.parse(uri.toString().replace("%20"," ").replace("%3A",":").replace("file://",""));
        EvidenceObject evdObj = EvidenceObject.fromMedia( uri);
       if(evdObjArr == null){ evdObjArr = new ArrayList<EvidenceObject>();}
        evdObjArr.add(evdObj);
       addToCaseLog( new Date()+":evidence Added");

        this.saveToUri(caseUri);
    }
    protected String getCaseTitle(){ return caseTitle;}
    protected Date getCreateDate(){return createDate;}
    protected Date getModifDate(){return modifDate;}
    protected ArrayList<String> getEvdidencesArr(){return evdidencesArr;}
    protected Uri getCaseUri(){return  Uri.parse(caseUri);}
    protected String getCaseStory(){return  caseStory;}
    protected void loadFromUri(Uri uri){
        CaseObject retrunCase;
        ObjectInputStream objectInputStream ;
        try {
            uri = Uri.parse(uri.toString().replace("%20"," "));
            String[] strings = uri.toString().split("/");
            File Dir = new File(strings[1]+"/"+strings[2]+"/"+strings[3]+"/"+strings[4]+"/"+strings[5]+"/"+strings[6]);

            File filer = new File(Dir, strings[7]);
            objectInputStream = new ObjectInputStream(new FileInputStream(filer));
            retrunCase =(CaseObject) objectInputStream.readObject();
            objectInputStream.close();
            caseFile = retrunCase.caseFile;
            caseUri = uri.toString();
            caseTitle = retrunCase.caseTitle;
            createDate = retrunCase.createDate;
            evdObjArr = retrunCase.evdObjArr;
            caseStory = retrunCase.caseStory;
            evdidencesArr = retrunCase.evdidencesArr;
            modifDate = retrunCase.modifDate;
            caseLog = retrunCase.getCaseLog();




            //no need for helper now.
            retrunCase = null;
        }catch(Exception e){
            retrunCase = null;

        }
    addToCaseLog(new Date()+": Loaded from memeory");
        //return retrunCase;
    }
    protected  boolean saveToUri(String uri){
        Boolean bool = true;
        setCaseTitle(caseTitle);
        setCaseStory(caseStory);


        try {
           // File chk = new File(String.valueOf(Paths.get(String.valueOf(uri))).replace("%20"," "));
            //File chk2 = new File(uri.toString());
            //TODO write the object correctly
            uri.replace("%20", " ");
            String[] strings = uri.toString().split("/", 8);
            File Dir = new File(strings[1]+"/"+strings[2]+"/"+strings[3]+"/"+strings[4]+"/"+strings[5]+"/"+strings[6]);
            if(!Dir.exists()){
                Dir.mkdirs();
            }
            Dir.list();
            File filer = new File(Dir, strings[7]);

          ////  chk2.createNewFile();//??
           // FileOutputStream fos = new FileOutputStream(chk2);
            //chk.mkdirs();chk.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(filer);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);//uri.toString()));
//            objectOutputStream.
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            bool = false;
            e.printStackTrace();
        }
        addToCaseLog(new Date()+":saved to memory");
        return bool;
    }
    protected File exportToFile(View v){
            generateExportableFile(v);
            addToCaseLog(new Date()+ ": file export called");
            return caseFile;
    }
    protected PdfDocument exportToPdf() {
           casePdf =  generateExportablePDF();
            return casePdf;
    }
    protected PdfDocument generateExportablePDF(){
        PdfDocument pdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100,100,1).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        String texts = getCaseTitle()+getCreateDate()+ getModifDate()+getCaseStory();//getTitle() + getDateCreate() + getDetail() + getDateModif();
        canvas.drawText(texts,10, 5, new Paint());
        pdf.finishPage(page);

        for (int n = 0 ; n < this.getEvdObjArr().size()-1; n++) {
            EvidenceObject obj = (EvidenceObject) evdObjArr.get(n);
            PdfDocument.Page pager = obj.createExportablePdfPage();
                PdfDocument.Page pagey = pdf.startPage( pager.getInfo());
                Canvas canvas1 = pagey.getCanvas();
                Canvas canvas2 = pager.getCanvas();
                canvas1 = canvas2;
                 //does this work??
                pdf.finishPage(pagey);
        }
          return pdf;
        }
    protected ArrayList<EvidenceObject> getEvdObjArr(){return  evdObjArr;}
    protected  void generateExportableFile(View v){
            //looking to generate a directory with the evidences as complete not just uri array
        ContextWrapper con = new ContextWrapper(v.getContext());
        File xDir = con.getDataDir();
        //create the "parent" folder of Case then add all the childs
        File yDir = new File(xDir, "Files/CaseExport_"+getCaseTitle());
        yDir.mkdirs();
        //makea de text file of case data
        File caseTexts = new File(yDir, "caseTexts");
        try {
            FileWriter fileWriter = new FileWriter(caseTexts);
            fileWriter.write(getCaseTitle()+getCaseStory()+getCreateDate()+getModifDate()+"\n"+getCaseLog() );
            fileWriter.close();
        }catch (Exception e){e.printStackTrace();}
        //run through the evidences and adda the filuhs
        if(getEvdidencesArr()!= null){
        for(int n = 0; n<getEvdidencesArr().size()-1; n++) {
            EvidenceObject evdobj = new EvidenceObject(Uri.parse(getEvdidencesArr().get(n)));
            File evdFile = evdobj.createExportableFile();

            try {
                evdFile.createNewFile();
                File zDir = new File(yDir, evdobj.getTitle());
                Files.move(zDir.toPath(), evdFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }
        yDir.exists();
        caseFile = yDir;

    };
    public  void onDestroy(){
        saveToUri(caseUri);
        //update cases/lasturi.las
        File datadir = Environment.getDataDirectory();

        File file = new File( datadir, "/user/0/com.beckettjs.take2evidentlydraft/files/lastURI.las");

        try {
            //clear contents
            file.delete();
            //set contents as caseUri.toString()
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(caseUri.toString());
            fw.close();
        }catch (Exception e){
            Integer chk=0; chk = chk +2;
        }
    }
    private void addToCaseLog(String str){
        caseLog = caseLog + "\n"+ str;
    }
    private String getCaseLog(){ return caseLog;}


}
