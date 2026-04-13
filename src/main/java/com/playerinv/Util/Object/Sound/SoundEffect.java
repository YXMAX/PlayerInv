package com.playerinv.Util.Object.Sound;

import com.playerinv.Util.NodeUtil;
import com.playerinv.Util.Object.Random.RandomFloat;
import org.bukkit.Sound;

import static com.playerinv.PlayerInv.*;
import static com.playerinv.Util.InitUtil.*;

public class SoundEffect {

    private float volume;

    private RandomFloat pitch;

    private Sound sound;

    private boolean enabled;

    private String path;

    private String original_sound;

    private boolean hasReplace;

    public SoundEffect(String path,float volume, RandomFloat pitch, Sound sound, boolean enabled) {
        this.path = path;
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
        this.enabled = enabled;
        this.hasReplace = false;
    }

    public SoundEffect(String path,float volume, RandomFloat pitch, String sound, boolean enabled, String replace112) {
        this.path = path;
        this.volume = volume;
        this.pitch = pitch;
        this.original_sound = sound;
        this.hasReplace = true;
        if(isBelow113){
            this.sound = Sound.valueOf(replace112);
        } else if(isBelow17){
            this.sound = Sound.valueOf("NOTE_PLING");
        } else {
            this.sound = Sound.valueOf(sound);
        }
        this.enabled = enabled;
    }

    public SoundEffect(String path,float volume, RandomFloat pitch, String sound, boolean enabled, String replace112,String replace113) {
        this.path = path;
        this.volume = volume;
        this.pitch = pitch;
        this.original_sound = sound;
        this.hasReplace = true;
        if(isBelow113){
            this.sound = Sound.valueOf(replace112);
        } else if(isBelow17){
            this.sound = Sound.valueOf("NOTE_PLING");
        } else if(is113){
            this.sound = Sound.valueOf(replace113);
        } else {
            this.sound = Sound.valueOf(sound);
        }
        this.enabled = enabled;
    }

    public void reloadSound(){
        this.enabled = plugin.getConfig().getBoolean("Sound." + path + ".Enabled");
        String value = plugin.getConfig().getString("Sound." + path + ".Value");
        if(value == null || NodeUtil.isEmpty(value) || !value.contains(":")){
            return;
        }
        String[] sValue_detail = value.split(":");
        if(!isBelow113){
            try {
                Sound sSound = Sound.valueOf(sValue_detail[0]);
                this.setSound(sSound);
            } catch (IllegalArgumentException e){
                NodeUtil.sendError("Path " + path + " invalid Sound: " + sValue_detail[0]);
                return;
            }
        } else if(!hasReplace){
            try {
                Sound sSound = Sound.valueOf(sValue_detail[0]);
                this.setSound(sSound);
            } catch (IllegalArgumentException e){
                NodeUtil.sendError("Path " + path + " invalid Sound: " + sValue_detail[0]);
                return;
            }
        } else {
            if(!sValue_detail[0].equalsIgnoreCase(this.original_sound)){
                try {
                    Sound sSound = Sound.valueOf(sValue_detail[0]);
                    this.setSound(sSound);
                } catch (IllegalArgumentException e){
                    NodeUtil.sendError("Path " + path + " invalid Sound: " + sValue_detail[0]);
                    return;
                }
            }
        }
        if(sValue_detail.length == 2){
            this.setVolume(Float.parseFloat(sValue_detail[1]));
        }
        if(sValue_detail.length == 3){
            this.setVolume(Float.parseFloat(sValue_detail[1]));
            if(sValue_detail[2].contains("-")){
                String[] split = sValue_detail[2].split("-");
                this.pitch = new RandomFloat(Float.parseFloat(split[0]),Float.parseFloat(split[1]));
            } else {
                this.pitch = new RandomFloat(Float.parseFloat(sValue_detail[2]));
            }
        }
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public RandomFloat getPitch() {
        return pitch;
    }

    public void setPitch(RandomFloat pitch) {
        this.pitch = pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
