package com.example.chris_000.endgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;



public class DialogMsgFragment extends Fragment {


    private ArrayList<FieldPoint> field;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_msg, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        field = (ArrayList<FieldPoint>) getArguments().get("field");
        Button buttonMsg = (Button) getView().findViewById(R.id.dialogMsgButton);
        buttonMsg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent changeActivity = new Intent(getActivity(), GameActivity.class);
                changeActivity.putExtra("field",field);
                startActivity(changeActivity);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}