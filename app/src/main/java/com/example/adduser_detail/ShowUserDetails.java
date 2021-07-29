package com.example.adduser_detail;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ShowUserDetails extends Fragment {
    View view;
    TextView tvusername,tvmobileno,tvdob,tvgender;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.showuserdetails,container,false);
        tvusername= view.findViewById(R.id.tv_show_user);
        tvmobileno=view.findViewById(R.id.tv_mobleno);
        tvdob=view.findViewById(R.id.tv_dob);
        tvgender=view.findViewById(R.id.tv_gender);

        return view;
    }
}
