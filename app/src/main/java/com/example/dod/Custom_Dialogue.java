package com.example.dod;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.allyants.notifyme.NotifyMe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Custom_Dialogue extends AppCompatDialogFragment {

    int t1Hour,t1minute;
    DatePickerDialog.OnDateSetListener setListener;
    Custom_DialogInterface dialogInterface;
    TextView time_set,date_set;
    EditText remainder_msg;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.reminder_dialogue,null);

        builder.setView(view)
                .setTitle("Add Reminder")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Set Reminder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        Toast.makeText(getContext(), "Open reminder again to see the current remainder", Toast.LENGTH_SHORT).show();

                        String msg = remainder_msg.getText().toString();
                        String time = time_set.getText().toString();
                        String date = date_set.getText().toString();

                        String id = fAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = fStore.collection("Reminder").document(id).collection("my_reminder").document();
                        Map<String,Object> data = new HashMap<>();
                        data.put("msg",msg);
                        data.put("time",time);
                        data.put("date",date);

                        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(getContext(), "done...", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(getContext(), "Error !" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogInterface.applyTexts(msg,time,date);

                    }
                });

        Calendar calendar = Calendar.getInstance();

        time_set = view.findViewById(R.id.time_set);
        date_set = view.findViewById(R.id.date_set);
        remainder_msg = view.findViewById(R.id.remainder_msg);

        time_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                calendar.set(Calendar.HOUR_OF_DAY,i);
                                calendar.set(Calendar.MINUTE,i1);

                                t1Hour = i;
                                t1minute = i1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t1Hour,t1minute);
                                time_set.setText(DateFormat.format("hh:mm aa",calendar));

                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(t1Hour,t1minute);
                timePickerDialog.show();
            }
        });


        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year, month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                NotifyMe notifyMe = new NotifyMe.Builder(getContext())
                        .title(remainder_msg.getText().toString())
                        .color(255,0,0,255)
                        .led_color(255,255,255,255)
                        .time(calendar)
                        .addAction(new Intent(),"Snooze",false)
                        .key("test")
                        .addAction(new Intent(),"Dismiss",true,false)
                        .addAction(new Intent(),"Done")
                        .large_icon(R.mipmap.ic_launcher_round)
                        .build();

                i1 = i1+1;
                String date = i2 + "/" + i1 + "/" + i;
                date_set.setText(date);
            }
        };

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        dialogInterface = (Custom_DialogInterface) context;

    }

    public interface Custom_DialogInterface{

        void applyTexts(String remainder_msg,String time_set, String date_set);

    }

}
