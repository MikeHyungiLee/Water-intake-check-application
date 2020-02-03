package com.scitmaster.miniproject.waterdietaryapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by leehyungi on 02/03/2018.
 */

public class MainMenuActivity extends Activity {

    Button waterMenu, calorieMenu, settingMenu;
    TextView returnLink, welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        waterMenu = findViewById(R.id.waterMenu);
        calorieMenu = findViewById(R.id.calorieMenu);
        settingMenu = findViewById(R.id.settingMenu);
        returnLink = findViewById(R.id.returnLink);
        welcomeText = findViewById(R.id.welcomeText);

        Intent intent = getIntent();

        //인텐트 값을 불러올때는 get 타입 Extra 로 선언해서 가져온다
        String s = intent.getStringExtra("returnValue");
        if(s != null) {
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        }

        returnLink.setTextColor(Color.BLUE);
        returnLink.setPaintFlags(returnLink.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        returnLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    public void waterMenu(View view){
        startActivity(new Intent(MainMenuActivity.this, WaterMenuActivity.class));
    }

    public void calorieMenu(View view){
        startActivity(new Intent(MainMenuActivity.this, CalorieMenuActivity.class));
    }

    public void settingMenu(View view){
        startActivity(new Intent(MainMenuActivity.this, SettingMenuActivity.class));
    }
}