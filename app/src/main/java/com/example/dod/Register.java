package com.example.dod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullname;
    EditText email;
    EditText password;
    EditText confirmpassword;
    TextView register;
    TextView loginpage;
    ProgressBar progressBar2;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        register = findViewById(R.id.login);
        loginpage = findViewById(R.id.loginpage);
        progressBar2 = findViewById(R.id.progressBar2);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String Confirm = confirmpassword.getText().toString();
                String name = fullname.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("password is Required");
                    return;
                }
                if(TextUtils.isEmpty(Confirm)){
                    confirmpassword.setError("Confirm password is Required");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    fullname.setError("Full name is Required");
                    return;
                }
                if(!Password.equals(Confirm)){
                    confirmpassword.setError("Password doesn't match");
                    return;
                }

                progressBar2.setVisibility(view.VISIBLE);

                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullname",name);
                            user.put("email",Email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "on success: user profile is created"+ userID);
                                }
                            });
                            Intent intent = new Intent(Register.this,Basic_info.class);
                            intent.putExtra("keyuid",userID);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(view.GONE);
                        }
                    }
                });
            }
        });
    }
}