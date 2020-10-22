package com.beckettjs.screenpathtest;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

class caseObject {
    //some values
    URI caseFolder;
    ArrayList<EvidenceObject> evidenceList;
    private  File caseLog;
    public caseObject onCreate(URI folderlocation){
        caseObject newCase = null;
        //tie folder to case
        newCase.caseFolder = folderlocation;
        //check for any contents of folder
        //if found, intialize evidence list
        //for each content (as folder),
                //create new evidence object
                //poulate title with "title.txt" ( as example)
                //scan for media in content, populate media of evidence,
                // repeat for desc, dates, etc
            //add evidence object to case arraylist of evidences
        //end foreach
        //newCase.caseLog = caseLog.txt
        //maintain caselog with accessed datetime
        return newCase;
    }
    public caseObject onCreate( ){
     caseObject newCase = new caseObject();
        //snap a new case with auto title date and name etc
        //create case folder in "default" location
        //newCase.caseFolder = "defualt";
        //create caselog in location, maintain creation datetime
    return newCase;
    }
    public caseObject createNewCase(String title, String description) {
        caseObject newDetailCase = null;
        newDetailCase.evidenceList = new ArrayList<EvidenceObject>();
        //creation date autofill stuff like that
        return newDetailCase;
    }
    public ArrayList<EvidenceObject> getEvidenceList(){
        return evidenceList;
    }
    protected void addEvidence( EvidenceObject e){
        this.evidenceList.add(e);
        //possibly toast some feedback
    }
    public void exportCaseDocument(){

        //set out case specific section
        //call each evidence to export, append to doc (organiseAS method)
        //in pdf case, organsise for export

        //ask export to where
        //export result to chosen destination
    }
    protected File organiseAsPDF(){
        File exportFile = null;
        //this looks to be tricky eh.
        return exportFile;
    }
    protected File folderTheCase(){
        File caseFolder = null;
        //generate folder-file arrangement
        return caseFolder;
    }
    protected File zipTheCase(File caseFile){
        File zippedCase = null;
        //compress casefolderfile
        return zippedCase;
    }
    protected EvidenceObject fetchEvidencePiece(URI evidenceURI){
        EvidenceObject evdfetch = new EvidenceObject();
        evdfetch.onCreate(evidenceURI);
        return evdfetch;
    }

}

