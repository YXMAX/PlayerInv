package com.playerinv.Util.Object.Temp;

public class TempMedium {

    private String uuid;

    private int num;

    private String inv;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getInv() {
        return inv;
    }

    public void setInv(String inv) {
        this.inv = inv;
    }

    public TempMedium(String uuid,int num,String inv){
        this.uuid = uuid;
        this.num = num;
        this.inv = inv;
    }
}
