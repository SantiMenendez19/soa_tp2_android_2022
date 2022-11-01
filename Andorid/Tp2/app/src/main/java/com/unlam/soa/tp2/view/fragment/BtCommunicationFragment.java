package com.unlam.soa.tp2.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unlam.soa.tp2.R;

public class BtCommunicationFragment extends Fragment {

    private static final String ARG_BT_DV_MAC_ADDRESS = "ARG_BT_DV_MAC_ADDRESS";

    public BtCommunicationFragment() {

    }
    public static BtCommunicationFragment newInstance(String param1) {
        BtCommunicationFragment fragment = new BtCommunicationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BT_DV_MAC_ADDRESS, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bt_communication, container, false);
    }
    