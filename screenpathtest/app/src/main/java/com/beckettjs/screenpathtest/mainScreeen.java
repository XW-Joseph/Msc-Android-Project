package com.beckettjs.screenpathtest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

class mainScreeen extends AppCompatActivity {
    //we need to definc a case type/classs
    //last case value holder
    //new case type
    protected void onCreate(Bundle savedInstState){
        super.onCreate(savedInstState);
        setContentView(R.layout.main_screen);
    }
    protected void openLastCase(){
        //so first we retrieve last case adress
        //if no last case, inform of no last case
        //then pass that to "case screen"
        // while switching focus
    }
    protected void snapLastCase(){
        //retreive last case adresss ( dupe, own method recoomend)
        //if no last case, handle
        //open floating widget with last case as destination for evidences
    }
    protected void snapNewCase(){
        //genereate new case file adress
        // open floating widget  ( dupe, own method)
    }
    protected void detailNewCase(){
        //generate new case file adress (dupe,own meth)
        //using case object "creat new" or initialize method
        //pass to detail screen with new case adress/object
    }
    protected void seeAllCases(){
        // pass to "all case screen"
    }
    protected caseObject fetchLastCase(){
      caseObject lastCase = null;
        // store caseobject last at defined address
      //get ref to case object
        //if not exist return null
      //return case object
        return lastCase;
    }
    protected void snapCaseOpen(caseObject caseToOpen){
        //intialize widget with ref to caseObject, this returns to home screen
    }

}
