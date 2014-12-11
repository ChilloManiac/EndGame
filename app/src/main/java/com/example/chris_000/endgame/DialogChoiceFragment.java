package com.example.chris_000.endgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class DialogChoiceFragment extends Fragment {

    private ArrayList<FieldPoint> field;
    private String gameName;
    private String player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_msg, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        field = (ArrayList<FieldPoint>) getArguments().get("field");
        gameName = (String) getArguments().get("name");
        player = (String) getArguments().get("player");

        Button buttonA = (Button) getView().findViewById(R.id.dialogChoiceButtonA);
        Button buttonB = (Button) getView().findViewById(R.id.dialogChoiceButtonB);

        buttonA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View anchorView) {
                LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_view_enddemo, null);

                final PopupWindow popupWindow = new PopupWindow(popupView,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                Button close = (Button) popupView.findViewById(R.id.popUp2Button);
                close.setOnClickListener(new View.OnClickListener() {
                    //onClick listener for popup window
                    public void onClick(View popupView) {
                        popupWindow.dismiss();
                    }
                });
                int location[] = new int[2];
                anchorView.getLocationOnScreen(location);
                popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                        location[0], location[1] + anchorView.getHeight());
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View anchorView) {
                LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_view_enddemo, null);

                final PopupWindow popupWindow = new PopupWindow(popupView,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                Button close = (Button) popupView.findViewById(R.id.popUp2Button);
                close.setOnClickListener(new View.OnClickListener() {
                    //onClick listener for popup window
                    public void onClick(View popupView) {
                        popupWindow.dismiss();
                    }
                });
                int location[] = new int[2];
                anchorView.getLocationOnScreen(location);
                popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                        location[0], location[1] + anchorView.getHeight());
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
