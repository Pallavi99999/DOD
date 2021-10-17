package com.example.dod;

public class food_item_value {
    String item,serving,calorie;

    public food_item_value(){
    }

    public food_item_value(String item, String serving, String calorie) {
        this.item = item;
        this.serving = serving;
        this.calorie = calorie;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }
}
