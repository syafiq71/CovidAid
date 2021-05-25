package com.android.covidaid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;


public class HomeFragment extends Fragment {


    private CardView cvCases,cvNews,cvNgo,cvAppointment;

    public HomeFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cvCases = view.findViewById(R.id.casesCV);
        cvNews = view.findViewById(R.id.NewsCv);
        cvNgo = view.findViewById(R.id.ngoCv);
        cvAppointment = view.findViewById(R.id.cvAppointment);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment covidStatusFragment = new covidStatusFragment();
                FragmentManager fragmentManager = getFragmentManager();
                 fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, covidStatusFragment,null )
                        .addToBackStack(null)
                        .commit();
            }
        });
        cvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newsFragment = new NewsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, newsFragment,null )
                        .addToBackStack(null)
                        .commit();
            }
        });
        cvNgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),BorangBantuan.class);
                startActivity(intent);

            }
        });

    }
}