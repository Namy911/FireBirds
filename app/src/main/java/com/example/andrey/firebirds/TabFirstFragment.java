package com.example.andrey.firebirds;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFirstFragment extends Fragment {


    public TabFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_first, container, false);

        if (savedInstanceState == null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_container, BirdsListFragment.newInstance("LLL"))
                    .commit();
        }
        return view;
    }

}
