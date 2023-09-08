package com.straccion.chat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.straccion.chat.R;
import com.straccion.chat.activities.FiltersActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment {

    View mView;
    CardView mcardviewNintendo;
    CardView mcardviewXBOX;
    CardView mcardviewPS4;
    CardView mcardviewPC;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FiltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FiltersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltersFragment newInstance(String param1, String param2) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_filters, container, false);
        mcardviewPC = mView.findViewById(R.id.cardviewPC);
        mcardviewNintendo = mView.findViewById(R.id.cardviewNintendo);
        mcardviewXBOX = mView.findViewById(R.id.cardviewXBOX);
        mcardviewPS4 = mView.findViewById(R.id.cardviewPS4);

        mcardviewPS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity("PS4");
            }
        });
        mcardviewXBOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity("XBOX");
            }
        });
        mcardviewNintendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity("NINTENDO");
            }
        });
        mcardviewPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity("PC");
            }
        });

        return mView;
    }

    private void goToFilterActivity(String category){
        Intent intent = new Intent(getContext(), FiltersActivity.class);
        intent.putExtra("categoria", category);
        startActivity(intent);
    }
}