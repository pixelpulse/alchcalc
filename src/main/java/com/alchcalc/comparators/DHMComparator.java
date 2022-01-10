package com.alchcalc.comparators;

import com.alchcalc.Ingredient;

import java.util.Comparator;

public class DHMComparator implements Comparator<Ingredient> {

    @Override
    public int compare(Ingredient i1, Ingredient i2) {
        return Double.compare(i2.getDHM(), i1.getDHM());
    }
}
