package com.example.develop.eatit.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.develop.eatit.Login;
import com.example.develop.eatit.R;


public class Home extends Fragment {

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




       Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra(Login.USER_NAME);





        View rootView =inflater.inflate(R.layout.fragment_home,container,false);

        return rootView;


    }


}
