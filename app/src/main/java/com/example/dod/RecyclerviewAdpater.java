package com.example.dod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerviewAdpater extends RecyclerView.Adapter<RecyclerviewAdpater.MyViewHolder> {

    Context context;
    ArrayList<food_item_value> food_item_valueArrayList;

    public RecyclerviewAdpater(Context context, ArrayList<food_item_value> food_item_valueArrayList) {
        this.context = context;
        this.food_item_valueArrayList = food_item_valueArrayList;
    }

    @NonNull
    @Override
    public RecyclerviewAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.food_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdpater.MyViewHolder holder, int position) {

        food_item_value food_item = food_item_valueArrayList.get(position);
        holder.item_name.setText(food_item.getItem());
        holder.cal_count.setText(food_item.getCalorie());
        holder.serving_count.setText(food_item.getServing());

    }

    @Override
    public int getItemCount() {
        return food_item_valueArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView item_name,cal_count,serving_count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name);
            cal_count = itemView.findViewById(R.id.cal_count);
            serving_count = itemView.findViewById(R.id.serving_count);
        }
    }

    public void filterList(ArrayList<food_item_value> filteredlist){
        food_item_valueArrayList = filteredlist;
        notifyDataSetChanged();
    }
}
