package com.playerinv.object.temp;

import org.bukkit.entity.Player;

import java.util.HashMap;

import static com.playerinv.util.LocaleUtil.getExpiryPlaceholderDisplay;

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
            return getExpiryPlaceholderDisplay(1);
        }
        long l = this.get(num);
        if(l > 0){
            return getExpiryPlaceholderDisplay(2).replace("%day_value%",String.valueOf(l));
        } else if(l == 0){
            return getExpiryPlaceholderDisplay(4);
        } else if(l == -1){
            return getExpiryPlaceholderDisplay(3);
        } else {
            return getExpiryPlaceholderDisplay(1);
        }
    }

    public String getDisplay(int num){
        long l = this.get(num);
        if(l > 0){
            return getExpiryPlaceholderDisplay(2).replace("%day_value%",String.valueOf(l));
        } else if(l == 0){
            return getExpiryPlaceholderDisplay(4);
        } else if(l == -1){
            return getExpiryPlaceholderDisplay(3);
        } else {
            return getExpiryPlaceholderDisplay(1);
        }
    }

    public void add(int num,long day){
        days.put(num,day);
    }
}
