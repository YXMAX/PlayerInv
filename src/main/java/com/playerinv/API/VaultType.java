package com.playerinv.API;

public enum VaultType {

    LARGE,

    MEDIUM;

    public int getType(){
        switch(this){
            case MEDIUM:
                return 2;
            case LARGE:
            default:
                return 1;
        }
    }
}
