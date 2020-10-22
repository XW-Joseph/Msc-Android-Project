package com.beckettjs.screenpathtest;

import java.io.File;
import java.net.URI;
import java.util.Date;

class EvidenceObject {

File mediaObject;
String evidenceTitle;
Date evidenceAddDate;
Date evidenceModDate;
String evidenceDescripion;
String evidenceId; //needed?
URI evidencelocation;

protected EvidenceObject onCreate(URI rootFolder){

    EvidenceObject newEvd = null;
    //scan for number of folders
    //create evidenceX folder ( x = n + 1 )
    //move to evidenceX folder
    //evidence title = some concat of rootfoldername and evidenceX
    //create title file
    //create date create and modified files
    //create description file
    //newEvd.URI = eviidenceX
    //newEvd.evidenceTitle = tile ... etc etc

    return newEvd;
}
protected File export(){
    File evdFile = null;
        //set filetype as (Eg) .pdf
        //write title, create date
        // if static media object ( img ) write media else.."non-static media request file directly "
        //write description

    return evdFile;
}

}
