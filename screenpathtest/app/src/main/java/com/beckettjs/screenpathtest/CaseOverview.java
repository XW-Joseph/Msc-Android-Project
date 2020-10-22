package com.beckettjs.screenpathtest;

class CaseOverview {

    caseObject currentCase = null;
    protected void addToCaseInstant(){
        //widget call
    }
    protected void addtoCaseFile(){
        //file select dialog
        //call currentcase to add file to evidence list
    }
    public void setCurrentCase(caseObject c){
        currentCase = c;
    }
    protected void openCaseLog(){
        //pass caselog to detail screen and open
    }
    protected void exportCase(){
        //currentCase.export();
    }

}
