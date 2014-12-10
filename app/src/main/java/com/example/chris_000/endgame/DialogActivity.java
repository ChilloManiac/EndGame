package com.example.chris_000.endgame;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogActivity extends FragmentActivity {

    private FragmentTransaction transaction;
    private android.support.v4.app.Fragment DialogMsgFragment;
    private String dialog1;
    private ArrayList<FieldPoint> field = null;
    private Boolean iWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            field = (ArrayList<FieldPoint>) extras.get("field");
            iWon = (Boolean) extras.get("won");
        }

        if (findViewById(R.id.dialogLayoutFrame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            DialogMsgFragment = new DialogMsgFragment();
            DialogMsgFragment.setArguments(extras);
            //DialogMsgFragment.setArguments(getIntent().getExtras());
            transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.dialogLayoutFrame, DialogMsgFragment).commit();
        }

        dialog1 = "Test test. \nBlaBalalablablabla.\nBla!";
        TextView textView = (TextView) findViewById(R.id.TextViewDialog);
        textView.setText(dialog1);

        if (field != null) {
            textView.setText("");
            for (FieldPoint fp :  field) {
                textView.append(fp.toString()+ "\n");
            }
        }
        if (iWon != null) {
            textView.setText(iWon.toString());
        }

    }



}
