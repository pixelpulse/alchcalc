package com.alchcalc;

import org.decimal4j.util.DoubleRounder;

import java.util.ArrayList;
import java.util.List;

public class Potion {
    Double DHAmount;
    List<Ingredient> potionIngredients = new ArrayList<>();
    Integer totalIngredients;

    public Double getDHAmount() {
        return DoubleRounder.round(DHAmount,3);
    }

    public void setDHAmount(Double DHAmount) {
        this.DHAmount = DHAmount;
    }

    public List<Ingredient> getIngredientList() {
        return potionIngredients;
    }

    public List<Ingredient> getPotionIngredients() {
        return potionIngredients;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.potionIngredients = ingredientList;
    }

    public Integer getTotalIngredients() {
        return totalIngredients;
    }

    public void setTotalIngredients(Integer totalIngredients) {
        this.totalIngredients = totalIngredients;
    }

    public void print(){
        System.out.println("DH: " + getDHAmount());
        for(Ingredient in:potionIngredients){
            System.out.println("Name:" + in.getName()
                    + " Amount:" + in.getQuantityInPotion());
        }
    }
}
