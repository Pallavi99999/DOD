package com.example.dod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class tracker extends AppCompatActivity {
    ProgressBar calorie_bar;
    private int progr;
    private String gen,active,gender;
    TextView cal_count,cal_required,reset_counter,message_user;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ImageView add_calorie;
    final Context context = this;
    ImageView back_button;
    private String total_cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        back_button = findViewById(R.id.back_button);
        calorie_bar = findViewById(R.id.calorie_bar);
        cal_count = findViewById(R.id.cal_count);
        cal_required = findViewById(R.id.cal_required);
        add_calorie = findViewById(R.id.add_calorie);
        reset_counter = findViewById(R.id.reset_counter);
        message_user = findViewById(R.id.message_user);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

        reset_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progr = 0;
                calorie_bar.setProgress(progr);
                cal_count.setText("0");
                progressData();
            }
        });

        String id_value = fAuth.getCurrentUser().getUid();
        fStore.collection("Basic_Detail").document(id_value).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String Gender_user = documentSnapshot.getString("gender");
                    String Age_user = documentSnapshot.getString("age");
                    String Activity_user = documentSnapshot.getString("activity");
                    calorie(Gender_user,Age_user,Activity_user);

                }else{
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "pagalllll", Toast.LENGTH_SHORT).show();
            }
        });

        reupdateprogressbar();


        add_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("ADD",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(userInput.getText().toString().isEmpty()){
                                            dialog.cancel();
                                        }
                                        else{
                                            String dekhte = userInput.getText().toString();
                                            progr = progr+Integer.parseInt(dekhte);
                                            //Toast.makeText(getApplicationContext(), "pro " + progr, Toast.LENGTH_SHORT).show();
                                            if (progr <= Integer.parseInt(total_cal)) {
                                                updateProgressBar();

                                            }else if(progr > Integer.parseInt(total_cal)){
                                                progr = progr-Integer.parseInt(dekhte);
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

        });

    }
    private void updateProgressBar(){
        calorie_bar.setProgress(progr);
        cal_count.setText(String.valueOf(progr));
    }

    private void calorie(String Gender_user,String Age_user,String Activity_user){
        gender = Gender_user;
        active = Activity_user;
        if(Integer.parseInt(Age_user) == 18){
            gen = "18";
        }else if(Integer.parseInt(Age_user) >= 19 && Integer.parseInt(Age_user) <= 20){
            gen = "19-20";
        }else if(Integer.parseInt(Age_user) >= 21 && Integer.parseInt(Age_user) <= 25){
            gen = "21-25";
        }else if(Integer.parseInt(Age_user) >= 26 && Integer.parseInt(Age_user) <= 30){
            gen = "26-30";
        }else if(Integer.parseInt(Age_user) >= 31 && Integer.parseInt(Age_user) <= 35){
            gen = "31-35";
        }else if(Integer.parseInt(Age_user) >= 36 && Integer.parseInt(Age_user) <= 40){
            gen = "36-40";
        }else{
            gen = "41-above";
        }

        fStore.collection("Calorie").document(gen).collection(gender).document(active).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String calorie = documentSnapshot.getString("cal");
                    calorie_bar.setMax(Integer.parseInt(calorie));
                    cal_required.setText(calorie);
                    total_cal = calorie;

                    message_user.setText("Since your age is " + Age_user + " and you are a " + Gender_user + " who has " + Activity_user + " Lifestyle. Therefore Your daily calorie intake should be : " + calorie);

                    //Toast.makeText(getApplicationContext(), "hd: " + calorie, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "pagalll", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "pagalllll", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reupdateprogressbar(){
        SharedPreferences getShared = getSharedPreferences("progress",MODE_PRIVATE);
        Integer val_prog = getShared.getInt("Int",0);
        Integer tot_val = getShared.getInt("tot",0);
        Toast.makeText(getApplicationContext(), "val_prog" + val_prog, Toast.LENGTH_SHORT).show();
        cal_count.setText(String.valueOf(val_prog));
        progr = val_prog;
        calorie_bar.setMax(tot_val);
        calorie_bar.setProgress(val_prog);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        progressData();
    }

    public void progressData(){
        Integer progress = progr;
        Integer total = Integer.parseInt(total_cal);
        SharedPreferences shrd = getSharedPreferences("progress",MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();
        editor.putInt("Int", progress);
        editor.putInt("tot",total);
        editor.apply();
    }
}