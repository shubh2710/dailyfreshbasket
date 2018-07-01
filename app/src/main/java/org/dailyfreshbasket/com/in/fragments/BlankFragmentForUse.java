package org.dailyfreshbasket.com.in.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dailyfreshbasket.com.in.R;

public class BlankFragmentForUse extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_fragment_for_use, viewGroup, false);
        //TextView output= (TextView)view.findViewById(R.id.msg1);
        //output.setText("Fragment One");
        return view;
    }
}

