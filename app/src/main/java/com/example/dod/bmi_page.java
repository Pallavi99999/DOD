package com.example.dod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class bmi_page extends AppCompatActivity {
    private ImageView back_button;
    private TextView bmi_text,nutri_text,ideal_text,user_name;
    private EditText weight_value;
    private Spinner height_value;
    private Button button_cal;
    private TextView bmi_value,nutri_value,ideal_value;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_page);
        back_button = findViewById(R.id.back_button);
        bmi_text = findViewById(R.id.bmi_text);
        nutri_text = findViewById(R.id.nutri_text);
        ideal_text = findViewById(R.id.ideal_text);
        weight_value = findViewById(R.id.weight_value);
        height_value = findViewById(R.id.height_value);
        button_cal = findViewById(R.id.button_cal);
        bmi_value = findViewById(R.id.bmi_value);
        nutri_value = findViewById(R.id.nutri_value);
        ideal_value = findViewById(R.id.ideal_value);
        user_name = findViewById(R.id.user_name);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

        //Bmi calculator

        button_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference doc = fstore.collection("weight_height").document(height_value.getItemAtPosition(height_value.getSelectedItemPosition()).toString());
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String height = documentSnapshot.getString("height");
                            String ideal_weight = documentSnapshot.getString("weight");

                            Double dec_height = Double.parseDouble(height) / 100;
                            Double sqr_height = dec_height * dec_height;
                            String userweight = weight_value.getText().toString();
                            if(userweight.isEmpty()){
                                Toast.makeText(getApplicationContext(), "Enter weight", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Double BMI = Double.parseDouble(userweight) / sqr_height;
                            BMI = Math.round(BMI * 100.0) / 100.0;
                            String Result = Double.toString(BMI);

                            bmi_value.setText("BMI : " + Result);

                            String condition;

                            if (Double.parseDouble(Result) < 18.5) {
                                condition = "Underweight";
                            } else if (Double.parseDouble(Result) >= 18.5 && Double.parseDouble(Result) < 25) {
                                condition = "Normal weight";
                            } else if (Double.parseDouble(Result) >= 25 && Double.parseDouble(Result) < 30) {
                                condition = "Overweight";
                            } else {
                                condition = "Obese";
                            }

                            nutri_value.setText("Nutritional status : " + condition);
                            ideal_value.setText("Ideal weight : " + ideal_weight);
                        }
                    }
                });
            }
        });



        String id = fAuth.getCurrentUser().getUid();
        //Toast.makeText(this, "id: "+ id, Toast.LENGTH_SHORT).show();
        DocumentReference document = fstore.collection("Basic_Detail").document(id);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String kg_weight = documentSnapshot.getString("weight");
                    String foot_height = documentSnapshot.getString("height");
                    String username = documentSnapshot.getString("name");

                    user_name.setText(username + " " + "status");

                    bmi(kg_weight,foot_height);

                }
            }
        });

    }

    private void bmi(String kg_weight, String foot_height){

        DocumentReference doc = fstore.collection("weight_height").document(foot_height);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String height = documentSnapshot.getString("height");
                    String ideal_weight = documentSnapshot.getString("weight");

                    Double dec_height = Double.parseDouble(height) / 100;
                    Double sqr_height = dec_height * dec_height;
                    Double BMI = Double.parseDouble(kg_weight) / sqr_height;
                    BMI = Math.round(BMI * 100.0) / 100.0;
                    String Result = Double.toString(BMI);

                    bmi_text.setText(Result);

                    String condition;

                    if (Double.parseDouble(Result) < 18.5) {
                        condition = "Underweight";
                    } else if (Double.parseDouble(Result) >= 18.5 && Double.parseDouble(Result) < 25) {
                        condition = "Normal weight";
                    } else if (Double.parseDouble(Result) >= 25 && Double.parseDouble(Result) < 30) {
                        condition = "Overweight";
                    } else {
                        condition = "Obese";
                    }

                    nutri_text.setText(condition);
                    ideal_text.setText(ideal_weight);
                }
            }
        });
    }

}