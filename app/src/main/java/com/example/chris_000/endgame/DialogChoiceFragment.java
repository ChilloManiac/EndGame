package com.example.chris_000.endgame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import static android.view.View.OnClickListener;

public class DialogChoiceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_choice, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Button buttonA = (Button) getView().findViewById(R.id.dialogChoiceButtonA);
        Button buttonB = (Button) getView().findViewById(R.id.dialogChoiceButtonB);
        Button buttonC = (Button) getView().findViewById(R.id.dialogChoiceButtonC);

        buttonA.setOnClickListener(new myClickListener());
        buttonB.setOnClickListener(new myClickListener());
        buttonC.setOnClickListener(new myClickListener());

        super.onViewCreated(view, savedInstanceState);
    }


    private class myClickListener implements OnClickListener {

        @Override
        public void onClick(View anchorView) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup_view_enddemo, null);

            final PopupWindow popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button close = (Button) popupView.findViewById(R.id.popUp2Button);
            close.setOnClickListener(new OnClickListener() {
                public void onClick(View popupView) {
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAtLocation(getView(), Gravity.CENTER, 0 ,0);
        }
    }

}
