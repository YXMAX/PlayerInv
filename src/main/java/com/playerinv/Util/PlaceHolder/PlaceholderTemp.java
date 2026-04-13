package com.playerinv.Util.PlaceHolder;

import org.bukkit.entity.Player;

import java.util.HashMap;

import static com.playerinv.Util.LocaleUtil.placeholder_expiry;

public class PlaceholderTemp {

    private HashMap<Integer,Long> days;

    /**
     * -1: 未拥有
     * 小于-1: 永久
     * 0: 小于一天
     * 大于0: 限时
     */
    public PlaceholderTemp() {
        days = new HashMap<>();
    }

    public long get(int num){
        if(days.containsKey(num)){
            return days.get(num);
        }
        return -1;
    }

    public String getDisplay(Player player, int num){
        if(player.isOp()){
            return placeholder_expiry(1);
        }
        long l = this.get(num);
        if(l > 0){
            return placeholder_expiry(2).replaceAll("%day_value%",String.valueOf(l));
        } else if(l == 0){
            return placeholder_expiry(4);
        } else if(l == -1){
            return placeholder_expiry(3);
        } else {
            return placeholder_expiry(1);
        }
    }

    public String getDisplay(int num){
        long l = this.get(num);
        if(l > 0){
            return placeholder_expiry(2).replaceAll("%day_value%",String.valueOf(l));
        } else if(l == 0){
            return placeholder_expiry(4);
        } else if(l == -1){
            return placeholder_expiry(3);
        } else {
            return placeholder_expiry(1);
        }
    }

    public void add(int num,long day){
        days.put(num,day);
    }
}
