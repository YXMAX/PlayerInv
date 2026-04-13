package com.playerinv.Util.Object.Random;

import java.math.BigDecimal;
import java.util.Random;

public class RandomDouble {

    private double min;

    private double max;

    public RandomDouble(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public RandomDouble(double max) {
        this.min = -1;
        this.max = max;
    }

    public RandomDouble() {
        this.min = 0;
        this.max = 2;
    }

    public double get(){
        if(this.min == -1){
            return this.max;
        }
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }
}
