package com.example.dod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Basic_info extends AppCompatActivity {
    private EditText name,age, weight;
    private Spinner gender,activity,height;
    private TextView submit;
    private ProgressBar progressBar3;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height_value);
        gender = findViewById(R.id.gender);
        submit = findViewById(R.id.submit);
        activity = findViewById(R.id.activity);
        progressBar3 = findViewById(R.id.progressBar3);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userID = getIntent().getStringExtra("keyuid");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Fullname = name.getText().toString();
                String userage = age.getText().toString();
                String userweight = weight.getText().toString();

                if(TextUtils.isEmpty(Fullname)){
                    name.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(userage)){
                    age.setError("Age is Required");
                    return;
                }
                if(TextUtils.isEmpty(userweight)){
                    weight.setError("Weight is Required");
                    return;
                }

                DocumentReference documentReference = fstore.collection("Basic_Detail").document(userID);
                Map<String,Object> data = new HashMap<>();
                data.put("name", name.getText().toString());
                data.put("gender", gender.getItemAtPosition(gender.getSelectedItemPosition()).toString());
                data.put("age", age.getText().toString());
                data.put("weight", weight.getText().toString());
                data.put("height", height.getItemAtPosition(height.getSelectedItemPosition()).toString());
                data.put("activity", activity.getItemAtPosition(activity.getSelectedItemPosition()).toString());

                documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "doneee", Toast.LENGTH_SHORT).show();
                        openDialogue();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Basic_info.this, "Error !" + e, Toast.LENGTH_SHORT).show();
                        progressBar3.setVisibility(view.GONE);
                    }
                });

                progressBar3.setVisibility(view.VISIBLE);

            }
        });



        //Toast.makeText(this, "userid: " + userID, Toast.LENGTH_SHORT).show();
    }

    private void openDialogue(){
        String id = fAuth.getCurrentUser().getUid();
        DocumentReference document = fstore.collection("Basic_Detail").document(id);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String weight = documentSnapshot.getString("weight");
                    String foot_height = documentSnapshot.getString("height");

                    bmi(weight,foot_height);

                }
            }
        });
    }

    private void bmi(String weight, String foot_height){
        DocumentReference doc = fstore.collection("weight_height").document(foot_height);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String height = documentSnapshot.getString("height");

                    Double dec_height = Double.parseDouble(height)/100;
                    Double sqr_height = dec_height * dec_height;
                    Double BMI = Double.parseDouble(weight) / sqr_height;
                    BMI = Math.round(BMI * 100.0) / 100.0;
                    String Result = Double.toString(BMI);
                    String condition;

                    if(Double.parseDouble(Result) < 18.5){
                        condition = "Underweight";
                    }else if(Double.parseDouble(Result) >= 18.5 && Double.parseDouble(Result) < 25){
                        condition = "Normal weight";
                    }else if(Double.parseDouble(Result) >= 25 && Double.parseDouble(Result) < 30){
                        condition = "Overweight";
                    }else{
                        condition = "Obese";
                    }
                    dialogue(Result, condition);

                    String userid = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("Diet_Plan").document(userid);
                    Map<String,Object> data = new HashMap<>();
                    data.put("bmi", Result);
                    data.put("status", condition);

                    documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Toast.makeText(getApplicationContext(), "oye hoye", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void dialogue(String message, String value){
        AlertDialog builder = new AlertDialog.Builder(Basic_info.this)
                .setTitle("Your status")
                .setMessage("\n" + "BMI: " + message + "\n" + "\n" + "Nutritional status: " + value)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        finish();
                    }
                }).create();
        builder.show();
        builder.setCanceledOnTouchOutside(false);

    }


}