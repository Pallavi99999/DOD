package com.example.dod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class reminderAdapter extends RecyclerView.Adapter<reminderAdapter.myviewholder> {

    ArrayList<reminder_model> reminderList;
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public reminderAdapter(ArrayList<reminder_model> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.item_msg.setText(reminderList.get(position).getMsg());
        holder.time_msg.setText(reminderList.get(position).getTime());
        holder.date_msg.setText(reminderList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{

        TextView item_msg,time_msg,date_msg;
        ImageView delete_reminder;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            item_msg = itemView.findViewById(R.id.item_msg);
            time_msg = itemView.findViewById(R.id.time_msg);
            date_msg = itemView.findViewById(R.id.date_msg);
            delete_reminder = itemView.findViewById(R.id.delete_reminder);

            delete_reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("hiii","bhagggg");
                    if(mListener!=null){
                        Log.d("hiiiiiiiiiiii","bhagggggg");
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            Log.d("hiiiiiiiiiiiiiiiiiiiiii","bhagggggg");
                            mListener.onDeleteClick(position);
                        }
                    }else{
                        Log.d("hiiiiii","bhagggggg");
                    }
                }
            });

        }
    }

}
