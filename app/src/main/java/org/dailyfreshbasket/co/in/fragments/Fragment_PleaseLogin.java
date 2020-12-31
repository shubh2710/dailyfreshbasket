package org.dailyfreshbasket.co.in.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.SignInOrLoginTabs;


public class Fragment_PleaseLogin extends Fragment {

    private Button login;
    private  Fragment fragment=null;

    @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_user_please_login, viewGroup, false);
            //TextView output= (TextView)view.findViewById(R.id.msg1);
            //output.setText("Fragment One");
            login=(Button)view.findViewById(R.id.bTologinPage);
            login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(getActivity(), SignInOrLoginTabs.class);
                startActivity(go);
                getActivity().finish();
            }
        });
            return view;
        }
    private void SetFragment(Fragment fragment){
        FragmentManager manager =getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.UserDetailsesFragrmentSwitcher, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}