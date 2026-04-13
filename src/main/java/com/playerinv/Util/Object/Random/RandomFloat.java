package com.playerinv.Util.Object.Random;

import java.math.BigDecimal;
import java.util.Random;

public class RandomFloat {

    private float min;

    private float max;

    private BigDecimal decimal;

    public RandomFloat(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public RandomFloat(float max) {
        this.min = -1;
        this.max = max;
    }

    public float get(){
        if(this.min == -1){
            return this.max;
        }
        Random rand = new Random();
        float result = min + rand.nextFloat() * (max - min);
        decimal = new BigDecimal(result);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
