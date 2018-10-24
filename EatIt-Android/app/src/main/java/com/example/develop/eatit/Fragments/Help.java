package com.example.develop.eatit.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.develop.eatit.Login;
import com.example.develop.eatit.R;


public class Help extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra(Login.USER_NAME);

        View rootView = inflater.inflate(R.layout.fragment_help,container,false);

        return rootView;
    }
}