package com.example.pawdaw.ordering_cleaning_products.model.fragments;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.pawdaw.ordering_cleaning_products.model.adapter.PlaceAdapterCustom;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;

import java.util.ArrayList;

/**
 * Created by pawdaw on 26/02/17.
 */

public class List_Fragment extends ListFragment {


    private  String INDEX = "index";

    // Currently selected item in the ListView
    private static int mCurCheckPosition = 0;

    // True or False depending on if we are in horizontal or duel pane mode
    private boolean landscape;

    //    Adapter
    private PlaceAdapterCustom placeAdapterCustom;

    
    private ArrayList<Place> userPlaces = new ArrayList<Place>();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt(INDEX, 0);
        }

        userPlaces = Storage.getInstance().getPlaces();

        placeAdapterCustom = new PlaceAdapterCustom(getActivity(), R.layout.row, userPlaces);
        setListAdapter(placeAdapterCustom);

        // CHOICE_MODE_SINGLE allows one item in the ListView to be selected at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Make the currently selected item highlighted
        getListView().setItemChecked(mCurCheckPosition, true);


        View detailFrame = getActivity().findViewById(R.id.description_layout);

        landscape = detailFrame !=null;
        if (landscape) {

            getFragmentManager().popBackStack();
            showDetails();
        }


    }


    // We save the last item selected in the list here and attach it to the key curChoice
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INDEX, mCurCheckPosition);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCurCheckPosition = position;
        showDetails();

    }


    void showDetails() {

        DescriptionFragment details = new DescriptionFragment();
        details.setIndex(mCurCheckPosition);


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Check if we are in horizontal mode and if yes show the ListView and
        // the Travel data
        if (landscape) {
            // ft.replace(R.id.flContainer, new_fragment);
            ft.replace(R.id.description_layout, details);
        }
        else {
//            // Replace whatever is in the fragment_container view with this fragment,   transaction.replace(R.id.fragment_container, newFragment);
//            // and add the transaction to the back stack
//            ft.replace(R.id.side_layout, details);
//
//            // addToBackStack(null) so your previous state will be added to the backstack allowing you to go back with the back button.
//            // MainActivity method onBackPressed() need to be implemented
//            ft.addToBackStack(null);
        }

        // Commit the changes
        ft.commit();
    }




}
