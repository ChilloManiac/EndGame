package com.example.chris_000.endgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DialogMsgFragment extends Fragment {

    Button buttonMsg = (Button) getView().findViewById(R.id.dialogMsgButton);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        buttonMsg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent changeActivity = new Intent(getActivity(), GameActivity.class);
                startActivity(changeActivity);
            }
        });

        return inflater.inflate(R.layout.fragment_dialog_msg, container, false);

    }
}