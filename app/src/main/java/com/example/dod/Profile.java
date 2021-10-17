package com.example.dod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    TextView namep,agep,genp,heip,weip,actp;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        namep = findViewById(R.id.namep);
        agep = findViewById(R.id.agep);
        genp = findViewById(R.id.genp);
        heip = findViewById(R.id.heip);
        weip = findViewById(R.id.weip);
        actp = findViewById(R.id.actp);
        back_button = findViewById(R.id.back_button);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String id = fAuth.getCurrentUser().getUid();

        fStore.collection("Basic_Detail").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("name");
                        String age = documentSnapshot.getString("age");
                        String gender = documentSnapshot.getString("gender");
                        String height = documentSnapshot.getString("height");
                        String weight = documentSnapshot.getString("weight");
                        String activity = documentSnapshot.getString("activity");


                        namep.setText(name);
                        agep.setText("Age"+" "+age);
                        genp.setText(gender);
                        heip.setText("Height"+" "+height);
                        weip.setText(weight+" "+"Kg");
                        actp.setText(activity);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("fail","failure");
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

    }
}