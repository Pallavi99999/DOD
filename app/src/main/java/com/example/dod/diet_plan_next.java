package com.example.dod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class diet_plan_next extends AppCompatActivity {
    ImageView back_button;
    RecyclerView recyclerview;
    TextView diet_heading;
    ArrayList<diet_plan_value> diet_plan_valueArrayList;
    Diet_Plan_Adatper diet_plan_adatper;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan_next);

        back_button = findViewById(R.id.back_button);
        recyclerview = findViewById(R.id.recyclerview);
        diet_heading = findViewById(R.id.diet_heading);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        diet_plan_valueArrayList = new ArrayList<>();
        diet_plan_adatper = new Diet_Plan_Adatper(diet_plan_next.this,diet_plan_valueArrayList);
        recyclerview.setAdapter(diet_plan_adatper);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String plan = getIntent().getStringExtra("key");
        diet_heading.setText(plan);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Diet_plan.class));
            }
        });

        String id = fAuth.getCurrentUser().getUid();
        fStore.collection("Diet_Plan").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String status = documentSnapshot.getString("status");

                fStore.collection("diet").document(status).collection(plan).orderBy("item", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            diet_plan_value obj = d.toObject(diet_plan_value.class);
                            diet_plan_valueArrayList.add(obj);
                        }
                        diet_plan_adatper.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
            }
        });

    }
}