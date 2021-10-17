package com.example.dod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reminder extends AppCompatActivity implements Custom_Dialogue.Custom_DialogInterface{
    ImageView back_button;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    final Context context = this;
    ArrayList<reminder_model> reminderList;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    reminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Toast.makeText(getApplicationContext(), "Click on add to set new Reminder", Toast.LENGTH_SHORT).show();

        back_button = findViewById(R.id.back_button);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.reminder_recycler);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderList = new ArrayList<>();
        adapter = new reminderAdapter(reminderList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new reminderAdapter.onItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

        String id = fAuth.getCurrentUser().getUid();

        fStore.collection("Reminder").document(id).collection("my_reminder").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            String documentId = d.getId();
                            reminder_model obj = d.toObject(reminder_model.class);
                            obj.setDocumentId(documentId);
                            reminderList.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Custom_Dialogue custom_dialogue = new Custom_Dialogue();
                custom_dialogue.show(getSupportFragmentManager(),"test custom dialogue");
                //startActivity(new Intent(getApplicationContext(),reminder_detail.class));

            }
        });
    }

    public void removeItem(int position){

        String id = fAuth.getCurrentUser().getUid();

        fStore.collection("Reminder").document(id).collection("my_reminder").document(reminderList.get(position).getDocumentId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("delete", "onComplete: deleted");
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("delete", "onFailure: not deleted");
                    }
                });

        reminderList.remove(position);
        adapter.notifyItemRemoved(position);

    }

    @Override
    public void applyTexts(String remainder_msg, String time_set, String date_set) {
    }
}