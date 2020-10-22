package com.beckettjs.take2evidentlydraft;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * A fragment representing a single CaseObject detail screen.
 * This fragment is either contained in a {@link CaseObjectListActivity}
 * in two-pane mode (on tablets) or a {@link CaseObjectDetailActivity}
 * on handsets.
 */
public class CaseObjectDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private CaseObject mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CaseObjectDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            // this is being passed null somewhow
//exceptio caused by jnullpointer exception
            try {


                mItem = new CaseObject(Uri.parse(getArguments().getString(ARG_ITEM_ID)));
            }catch(Exception e){
                Integer debugHook = 2 + 1;
            }
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                try {
                    appBarLayout.setTitle(mItem.caseTitle);
                }catch (Exception e){
                    Integer dbugHook = 1 + 2;
                }
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.caseobject_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.caseobject_detail)).setText(mItem.caseStory);
        }

        return rootView;
    }
}
