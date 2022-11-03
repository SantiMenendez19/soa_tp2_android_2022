package com.unlam.soa.tp2.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unlam.soa.tp2.R;

public class BtUnavailableFragment extends Fragment {


    public BtUnavailableFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bt_unavailable, container, false);
    }
}