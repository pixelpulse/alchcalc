package com.alchcalc;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        try {
            calculator.run(Task.DH, 10);
        }
        catch(Exception e){
            System.out.println(e);
        }


    }
}
