package com.beckettjs.take2evidentlydraft;
// thanks to https://guides.codepath.com/android/using-the-recyclerview (25/11/2019)

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class myVisListAdapter extends RecyclerView.Adapter<myVisListAdapter.myViewHolder> {

    private List<EvidenceObject> dataset;

    protected class myViewHolder extends RecyclerView.ViewHolder{
        protected TextView eleTitle;
        protected Button eleButt;

        public myViewHolder(View eleView){
            super(eleView);
            eleTitle = (TextView) eleView.findViewById(R.id.evdTitleListEle);
            eleButt = (Button) eleView.findViewById(R.id.toEvdEleBut);
        }
    }
    //do we pass a uri list and reconstruct or just the objects...
    public myVisListAdapter(List<EvidenceObject> elementsList){
        dataset = elementsList;
    }
    @Override
    public myVisListAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from((context));
        View viewer = inflater.inflate(R.layout.evdrowlayout, parent,false);//false
        viewer.hasOverlappingRendering();

        viewer.forceHasOverlappingRendering(false);


        viewer.offsetTopAndBottom(10);
        myViewHolder viewHolder = new myViewHolder(viewer) ;

        //this might be wehre i need to set them from not ovelapping
        return viewHolder;
    }

    @Override
    public  void onBindViewHolder(myVisListAdapter.myViewHolder viewHolder, int pos){
        EvidenceObject evdObj = dataset.get(pos);

        TextView textView = viewHolder.eleTitle;
        if(evdObj.getDateCreate() != null) {
            textView.setText(evdObj.getDateCreate().toString());
        }else{ textView.setText("filler text");
        }
        Button butt = viewHolder.eleButt;
        butt.setText("observe");
        //TODO make the button do somehtiibg like view the evidence lol
        butt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToEvidenceView(pos,v);
            }
        });

    }
    public void goToEvidenceView(int pos, View v){
        EvidenceObject evidenceObject = dataset.get(pos);
        String evdUri = evidenceObject.getObjectUriAsString();
        Intent intent = new Intent( v.getContext() ,EvdDetailView.class);
        intent.putExtra("evdUri", evidenceObject.getObjectUriAsString());
        v.getContext().startActivity(intent);
    }
        @Override
    public int getItemCount() {
        return dataset.size();
    }
}
