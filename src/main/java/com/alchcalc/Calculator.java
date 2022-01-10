package com.alchcalc;

import com.alchcalc.comparators.DHMComparator;
import org.decimal4j.util.DoubleRounder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Calculator {
    IngredientHandler ingredientHandler = new IngredientHandler();

    private Potion currentBestPotion = new Potion();
    private List<Ingredient> allIngredients = new ArrayList<>();

    //We store the best base, best adders and multiplier ingredients here to cut down on unnecessary calculations
    private List<Ingredient> bestMultipliers = new ArrayList<>();
    private List<Ingredient> bestAdders = new ArrayList<>();
    private List<Ingredient> bestAdderMultiplier = new ArrayList<>();
    private Ingredient bestBase;

    public void run(Task task) throws IOException {
        makeBestPotion(task);
    }

    private void makeBestPotion(Task task){
        allIngredients = ingredientHandler.getIngredientList();
        setBestIngredients(task);

//        findOptimizedBaseIngredients(10, true);

        //printing our results. We'll need to get them off the potion eventually
        (optimizeVolumizers(10, true)).print();
    }

    private Potion optimizeMultipliers(){
        Potion bestPotion = new Potion();
        return bestPotion;
    }

    private Potion optimizeVolumizers(Integer potionUnits, Boolean isCladeGift){
        List<Ingredient> potionIngredients = new ArrayList<>();
        Integer totalVolumeNeeded = getVolumeNeeded(potionUnits, isCladeGift);
        Potion currentPotion;
        Potion bestPotion;

        //add our best volumizing ingredients to a list fot the potion
        potionIngredients.add(bestBase);
        potionIngredients.addAll(bestAdderMultiplier);
        potionIngredients.addAll(bestAdders);
//        potionIngredients.addAll(bestMultipliers);

        bestBase.setQuantityInPotion(totalVolumeNeeded);
        bestPotion = brewPotion(potionIngredients, totalVolumeNeeded);

        //We iterate through adding ingredients (and replacing one base) one at a time to see if the potion has been improved
        for(Ingredient ingredient: potionIngredients.subList( 1, potionIngredients.size() )){
            Boolean potionImproving = true;

            while(potionImproving){
                //We add one of our current ingredient and remove one from base (which starts off as the entire potion)
                ingredient.setQuantityInPotion(ingredient.getQuantityInPotion()+1);
                bestBase.setQuantityInPotion(bestBase.getQuantityInPotion()-1);

//                System.out.println("testing ingredient:" +ingredient.getName()+ " with count:"+ ingredient.getQuantityInPotion());

                currentPotion = brewPotion(potionIngredients,totalVolumeNeeded);

                //We compare the current potion with the base potion, if it's worse we continue
                if(currentPotion.getDHAmount() > bestPotion.getDHAmount()){
                    bestPotion = currentPotion;
                }
                else{
                    potionImproving = false;
                }
            }
        }
        return bestPotion;
    }


    private Potion brewPotion(List<Ingredient> potionIngredients, Integer totalIngredientCount){
        //[(1 + Ax^.5) * (1 + Ax^.5) >>>>>>] * DH
        //x is the % of the potion the item takes up
        //A is the items respective multiplicative value. such as .21 for white cavolo.

        Potion brewedPotion = new Potion();

        List<Double> multipliers = new ArrayList<>();
        List<Double> directhealers = new ArrayList<>();

        Double multiplierAggregated = 0.0;
        Double dhAggregated = 0.0;

        //will wrap this into Task logic specific to DH TODO
        for(Ingredient ingredient:potionIngredients){
            if(ingredient.getDHM()>0 && ingredient.getQuantityInPotion()!=0){
                Double A = ingredient.getDHM();
                Double x = (double)ingredient.getQuantityInPotion()/(double)totalIngredientCount;
                Double product = 1+A*Math.sqrt(x);
                multipliers.add(product);
            }
            if(ingredient.getDH()>0){
                directhealers.add(ingredient.getQuantityInPotion() * ingredient.getDH());
            }
        }

        //We multiply all the multipliers
        for(Double multiplier: multipliers){
            if(multiplierAggregated == 0.0){
                multiplierAggregated = multiplier;
            }
            else{
                multiplierAggregated = multiplierAggregated*multiplier;
            }
        }

        //We get the aggregatedDH
        for(Double directhealer: directhealers){
            if(dhAggregated == 0.0){
                dhAggregated = directhealer;
            }
            else{
                dhAggregated = dhAggregated+directhealer;
            }
        }
        dhAggregated = dhAggregated/totalIngredientCount;


        if(multiplierAggregated!=0){
            brewedPotion.setDHAmount(multiplierAggregated*dhAggregated);
        }
        else brewedPotion.setDHAmount(dhAggregated);

        brewedPotion.setIngredientList(potionIngredients);
        return brewedPotion;
    }


    private void setBestIngredients(Task task){
        switch (task){
            case DH:
                //We get the best base
                for (Ingredient ing:allIngredients) {
                    if(null == bestBase){
                        bestBase = ing;
                    }
                    else if(ing.getDH() > bestBase.getDH()){
                        bestBase = ing;
                    }
                }

                //Find the max number of multipliers which exist
                Integer multiplierCount = 0;
                for(Ingredient ingredient: allIngredients){
                    if(ingredient.getDHM()>0){
                        multiplierCount+=1;
                    }
                }
                Integer maxAmountofMultipliers;
                if(multiplierCount<15){
                    maxAmountofMultipliers = multiplierCount;
                }
                else maxAmountofMultipliers = 14;

                //We get the best 15 or less multiplies
                DHMComparator dhmComparator = new DHMComparator();
                allIngredients.sort(dhmComparator);
                for(int i=0; i<maxAmountofMultipliers; i++){
                    bestMultipliers.add(allIngredients.get(i));
                }

                //We add our ingredients that are both Adder & Multiplier (eg. Musefruit)
                for(Ingredient ingredient: allIngredients){
                    if(ingredient.getDH() > 0.0 && ingredient.getDHM() > 0.0){
                        bestAdderMultiplier.add(ingredient);
                    }
                }
                break;

            case DP:
        }

        //We split out our Adders from the multipliers
        for(Ingredient ing: bestMultipliers){
            if(ing.getAdder()){
                bestAdders.add(ing);
            }
        }
        bestMultipliers.removeAll(bestAdders);
    }
    private Integer getVolumeNeeded(Integer potionUnits, Boolean isCladeGift){
        Integer totalVolumeNeeded = 0;
        switch (potionUnits){
            case 10:
                if(isCladeGift){
                    totalVolumeNeeded = 101;
                }
                else{
                    totalVolumeNeeded = 101;
                }
                break;
            case 20:
                if(isCladeGift){
                    totalVolumeNeeded = 201;
                }
                else{
                    totalVolumeNeeded = 191;
                }
                break;
            case 30:
                if(isCladeGift){
                    totalVolumeNeeded = 281;
                }
                else{
                    totalVolumeNeeded = 101;
                }
                break;
            case 40:
                if(isCladeGift){
                    totalVolumeNeeded = 371;
                }
                else{
                    totalVolumeNeeded = 401;
                }
                break;
            case 50:
                if(isCladeGift){
                    totalVolumeNeeded = 461;
                }
                else{
                    totalVolumeNeeded = 501;
                }
                break;
            case 60:
                if(isCladeGift){
                    totalVolumeNeeded = 551;
                }
                else{
                    totalVolumeNeeded = 601;
                }
                break;
            case 70:
                if(isCladeGift){
                    totalVolumeNeeded = 641;
                }
                else{
                    totalVolumeNeeded = 701;
                }
                break;
            case 80:
                if(isCladeGift){
                    totalVolumeNeeded = 731;
                }
                else{
                    totalVolumeNeeded = 821;
                }
                break;
            case 90:
                if(isCladeGift){
                    totalVolumeNeeded = 821;
                }
                else{
                    totalVolumeNeeded = 90;
                }
                break;
            case 100:
                if(isCladeGift){
                    totalVolumeNeeded = 911;
                }
                else{
                    totalVolumeNeeded = 1001;
                }
                break;
            case 200:
                if(isCladeGift){
                    totalVolumeNeeded = 1521;
                }
                else{
                    totalVolumeNeeded = 2001;
                }
                break;
        }
        return totalVolumeNeeded;
    }

}
