package com.example.chris_000.endgame;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

public class DialogActivity extends FragmentActivity {

    private FragmentTransaction transaction;
    private android.support.v4.app.Fragment DialogMsgFragment;
    private String dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        if (findViewById(R.id.dialogLayoutFrame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            DialogMsgFragment = new DialogMsgFragment();
            DialogMsgFragment.setArguments(getIntent().getExtras());
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.dialogLayoutFrame, DialogMsgFragment);
            transaction.commit();
        }

        dialog1 = "Test test. \nBlaBalalablablabla.\nBla!";
        TextView textView = (TextView)findViewById(R.id.TextViewDialog);
        textView.setText(dialog1);

    }



}
