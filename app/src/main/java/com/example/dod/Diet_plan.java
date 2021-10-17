package com.example.dod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Diet_plan extends AppCompatActivity {
    ImageView back_button;
    TextView break_fast,lunch_time,evening_time,dinner_time,start_msg;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        back_button = findViewById(R.id.back_button);
        break_fast = findViewById(R.id.break_fast);
        lunch_time = findViewById(R.id.lunch_time);
        evening_time = findViewById(R.id.evening_time);
        dinner_time = findViewById(R.id.dinner_time);
        start_msg = findViewById(R.id.start_msg);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        String userid = fAuth.getCurrentUser().getUid();
        fstore.collection("Diet_Plan").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String condition = documentSnapshot.getString("status");
                    start_msg.setText("Since you have " + condition + ". Following is your diet plan");
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));

            }
        });

        break_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),diet_plan_next.class);
                intent.putExtra("key","Breakfast");
                startActivity(intent);

            }
        });

        lunch_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),diet_plan_next.class);
                intent.putExtra("key","Lunch");
                startActivity(intent);
            }
        });

        evening_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),diet_plan_next.class);
                intent.putExtra("key","Evening");
                startActivity(intent);
            }
        });

        dinner_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),diet_plan_next.class);
                intent.putExtra("key","Dinner");
                startActivity(intent);
            }
        });
    }
}