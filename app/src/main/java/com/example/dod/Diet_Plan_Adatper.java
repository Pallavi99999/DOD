package com.example.dod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Diet_Plan_Adatper extends RecyclerView.Adapter<Diet_Plan_Adatper.mynewViewHolder>{

    Context context;
    ArrayList<diet_plan_value> diet_plan_valueArrayList;

    public Diet_Plan_Adatper(Context context, ArrayList<diet_plan_value> diet_plan_valueArrayList) {
        this.context = context;
        this.diet_plan_valueArrayList = diet_plan_valueArrayList;
    }

    @NonNull
    @Override
    public Diet_Plan_Adatper.mynewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View z = LayoutInflater.from(context).inflate(R.layout.diet_food_item_list,parent,false);
        return new mynewViewHolder(z);
    }

    @Override
    public void onBindViewHolder(@NonNull mynewViewHolder holder, int position) {
        diet_plan_value diet_food_item_list = diet_plan_valueArrayList.get(position);
        holder.diet_item.setText(diet_food_item_list.getItem());
    }

    @Override
    public int getItemCount() {
        return diet_plan_valueArrayList.size();
    }

    class mynewViewHolder extends RecyclerView.ViewHolder{

        TextView diet_item;

        public mynewViewHolder(@NonNull View itemView){
            super(itemView);

            diet_item = itemView.findViewById(R.id.diet_item);
        }

    }
}
