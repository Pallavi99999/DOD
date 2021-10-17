package com.example.dod;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Food_calorie extends AppCompatActivity {
    private ImageView back_button;
    RecyclerView recyclerView;
    ArrayList<food_item_value> food_item_valueArrayList;
    RecyclerviewAdpater recyclerviewAdpater;
    FirebaseFirestore fstore;
    ProgressDialog progressDialog;
    EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_calorie);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        search_bar = findViewById(R.id.search_bar);
        back_button = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        fstore = FirebaseFirestore.getInstance();
        food_item_valueArrayList = new ArrayList<>();
        recyclerviewAdpater = new RecyclerviewAdpater(Food_calorie.this,food_item_valueArrayList);
        recyclerView.setAdapter(recyclerviewAdpater);

        //search bar
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        //recyclerview
        fstore.collection("Food").orderBy("item", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            food_item_value obj = d.toObject(food_item_value.class);
                            food_item_valueArrayList.add(obj);
                        }
                        recyclerviewAdpater.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                                progressDialog.dismiss();
                    }
                });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

    }

    private void filter(String text){
        ArrayList<food_item_value> filteredlist = new ArrayList<>();
        for(food_item_value item:food_item_valueArrayList){
            if(item.getItem().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(item);
            }
        }
        recyclerviewAdpater.filterList(filteredlist);
    }
}