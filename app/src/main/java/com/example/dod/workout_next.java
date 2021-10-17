package com.example.dod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pl.droidsonroids.gif.GifImageView;

public class workout_next extends AppCompatActivity {
    String buttonvalue;
    Button startbutton;
    private CountDownTimer countDownTimer=null;
    TextView mtextview;
    private Boolean MTimeRunning=false;
    private long MTimeLeftMills;
    TextView step1,step2,step3,step4;
    ImageView back_button;
    FirebaseFirestore fstore;
    private MediaPlayer mediaplayer;
    GifImageView exercise_1,exercise_2,exercise_3,exercise_4,exercise_5,exercise_6,exercise_7,exercise_8,exercise_9,exercise_10,exercise_11,exercise_12,exercise_13,exercise_14,exercise_15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_next);

        startbutton = findViewById(R.id.startbutton);
        mtextview = findViewById(R.id.time);

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        exercise_1 = findViewById(R.id.exercise_1);
        exercise_2 = findViewById(R.id.exercise_2);
        exercise_3 = findViewById(R.id.exercise_3);
        exercise_4 = findViewById(R.id.exercise_4);
        exercise_5 = findViewById(R.id.exercise_5);
        exercise_6 = findViewById(R.id.exercise_6);
        exercise_7 = findViewById(R.id.exercise_7);
        exercise_8 = findViewById(R.id.exercise_8);
        exercise_9 = findViewById(R.id.exercise_9);
        exercise_10 = findViewById(R.id.exercise_10);
        exercise_11 = findViewById(R.id.exercise_11);
        exercise_12 = findViewById(R.id.exercise_12);
        exercise_13 = findViewById(R.id.exercise_13);
        exercise_14 = findViewById(R.id.exercise_14);
        exercise_15 = findViewById(R.id.exercise_15);
        back_button = findViewById(R.id.back_button);

        mediaplayer = MediaPlayer.create(this, R.raw.background);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),workout.class));
            }
        });

        //start

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MTimeRunning){
                    stopTimer();
                    mediaplayer.pause();
                }else{
                    startTimer();
                    mediaplayer.start();
                }
            }
        });


        buttonvalue = getIntent().getStringExtra("value");
        fstore = FirebaseFirestore.getInstance();

        fstore.collection("workout_how_to_do").document(buttonvalue).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String step11 = documentSnapshot.getString("step1");
                    String step22 = documentSnapshot.getString("step2");
                    String step33 = documentSnapshot.getString("step3");
                    String step44 = documentSnapshot.getString("step4");


                    step1.setText("step1: "+step11);
                    step2.setText("step2: "+step22);
                    step3.setText("step3: "+step33);
                    step4.setText("step4: "+step44);

                    switch (Integer.parseInt(buttonvalue)){
                        case 1:
                            exercise_1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            exercise_2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            exercise_3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            exercise_4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            exercise_5.setVisibility(View.VISIBLE);
                            break;
                        case 6:
                            exercise_6.setVisibility(View.VISIBLE);
                            break;
                        case 7:
                            exercise_7.setVisibility(View.VISIBLE);
                            break;
                        case 8:
                            exercise_8.setVisibility(View.VISIBLE);
                            break;
                        case 9:
                            exercise_9.setVisibility(View.VISIBLE);
                            break;
                        case 10:
                            exercise_10.setVisibility(View.VISIBLE);
                            break;
                        case 11:
                            exercise_11.setVisibility(View.VISIBLE);
                            break;
                        case 12:
                            exercise_12.setVisibility(View.VISIBLE);
                            break;
                        case 13:
                            exercise_13.setVisibility(View.VISIBLE);
                            break;
                        case 14:
                            exercise_14.setVisibility(View.VISIBLE);
                            break;
                        case 15:
                            exercise_15.setVisibility(View.VISIBLE);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "coming soon...", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void stopTimer(){
        countDownTimer.cancel();
        MTimeRunning=false;
        startbutton.setText("START");
    }
    private void startTimer(){
        MTimeRunning = true;
        final CharSequence value1 = mtextview.getText();
        String num1 = value1.toString();
        String num2 = num1.substring(0,2);
        String num3 = num1.substring(3,5);

        final int number = Integer.valueOf(num2) * 60 + Integer.valueOf(num3);
        MTimeLeftMills = number*1000;

        countDownTimer = new CountDownTimer(MTimeLeftMills,1000) {
            @Override
            public void onTick(long l) {
                MTimeLeftMills = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                int newValue = Integer.valueOf(buttonvalue)+1;
                if(newValue<=7){
                    Intent intent = new Intent(workout_next.this,workout_next.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newValue));
                    mediaplayer.stop();
                    startActivity(intent);
                }else{
                    newValue = 1;
                    Intent intent = new Intent(workout_next.this,workout_next.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newValue));
                    mediaplayer.stop();
                    startActivity(intent);
                }
            }
        }.start();
        startbutton.setText("PAUSE");
    }

    private void updateTimer(){
        int minutes = (int) MTimeLeftMills/60000;
        int seconds = (int) MTimeLeftMills%60000 /1000;

        String timeLeftText="";
        if(minutes<10){
            timeLeftText = "0";
        }
        timeLeftText = timeLeftText + minutes+":";
        if(seconds<10){
            timeLeftText = "0";
        }
        timeLeftText+= seconds;
        mtextview.setText(timeLeftText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaplayer.stop();
    }
}