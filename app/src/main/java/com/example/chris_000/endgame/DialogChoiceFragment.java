package com.example.chris_000.endgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DialogChoiceFragment extends Fragment {

    Button buttonA = (Button) getView().findViewById(R.id.dialogChoiceButtonA);
    Button buttonB = (Button) getView().findViewById(R.id.dialogChoiceButtonB);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //buttonA.setOnClickListener();
        //buttonB.setOnClickListener();

        return inflater.inflate(R.layout.fragment_dialog_msg, container, false);
    }
}