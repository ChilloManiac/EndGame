package com.example.chris_000.endgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DialogChoiceFragment extends Fragment {

    Button button = (Button) getView().findViewById(R.id.dialogFragmentButton);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //button.setOnClickListener();

        return inflater.inflate(R.layout.fragment_dialog_msg, container, false);
    }
}