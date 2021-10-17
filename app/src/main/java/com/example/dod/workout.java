package com.example.dod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class workout extends AppCompatActivity {
    ImageView back_button;

    int[] newArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

        newArray = new int[]{
                R.id.mountain_pose,R.id.basic_pose,R.id.bench_pose,R.id.bicycle_pose,R.id.leg_pose,R.id.alterheel_pose,
                R.id.legup_pose,R.id.sit_pose,R.id.alterv_pose,R.id.plankrot_pose,R.id.plankleg_pose,R.id.russian_pose,
                R.id.bridge_pose,R.id.vertleg_pose,R.id.wind_pose
        };

    }

    public void Activityclick(View view){
        for(int i=0;i< newArray.length;i++){
            if(view.getId() == newArray[i]){
                int value = i + 1;
                Intent intent = new Intent(getApplicationContext(),workout_next.class);
                intent.putExtra("value",String.valueOf(value));
                startActivity(intent);
            }
        }

    }
}