package com.scitmaster.miniproject.waterdietaryapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by leehyungi on 03/03/2018.
 */

public class CalorieMenuActivity extends AppCompatActivity {

    TextView returnLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dietary_calorie);

        returnLink = findViewById(R.id.returnLink);

        returnLink.setTextColor(Color.BLUE);
        returnLink.setPaintFlags(returnLink.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        returnLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalorieMenuActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

    }
}
