package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class IntroductionActivity extends Activity {

    private ArrayList<FieldPoint> field;
    private String player;
    private TextView textBox;
    private Button nextButton;
    private String gameName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            field = (ArrayList<FieldPoint>) extras.get("field");
            gameName = (String) extras.get("name");
            player = (String) extras.get("player");
        }
        textBox = (TextView) findViewById(R.id.introduction_text_view);

        if (player.equals("player1")) {
            textBox.setText(this.getString(R.string.introductionAllies));
        } else if (player.equals("player2")) {
            textBox.setText(this.getString(R.string.introductionAxis));
        }

        nextButton = (Button) findViewById(R.id.introduction_next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(IntroductionActivity.this, DialogActivity.class);
                changeActivity.putExtra("field", field);
                changeActivity.putExtra("name", gameName);
                changeActivity.putExtra("player", player);
                IntroductionActivity.this.startActivity(changeActivity);



            }
        });





    }

}
