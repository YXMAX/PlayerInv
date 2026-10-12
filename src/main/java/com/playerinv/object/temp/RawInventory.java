package com.playerinv.object.temp;

public class RawInventory {

    public int num;
    public String invBase64;

    public RawInventory(int num, String invBase64) {
        this.num = num;
        this.invBase64 = invBase64;
    }

    public int getSize(){
        return this.invBase64.length();
    }
}
