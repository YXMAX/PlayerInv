package com.playerinv.object.sound;

import com.playerinv.util.NodeUtil;
import com.playerinv.object.random.RandomFloat;
import com.playerinv.util.VersionUtil;
import org.bukkit.Sound;

import static com.playerinv.PlayerInv.*;

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
        if(VersionUtil.isVer1_7()){
            this.sound = Sound.valueOf("NOTE_PLING");
        } else if(VersionUtil.isVer1_12_2()){
            this.sound = Sound.valueOf(replace112);
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
        if(VersionUtil.isVer1_7()){
            this.sound = Sound.valueOf("NOTE_PLING");
        } else if(VersionUtil.isVer1_12_2()){
            this.sound = Sound.valueOf(replace112);
        } else if(VersionUtil.isVer1_13()){
            this.sound = Sound.valueOf(replace113);
        } else {
            this.sound = Sound.valueOf(sound);
        }
        this.enabled = enabled;
    }

    public void reloadSound(){
        this.enabled = getInstance().getConfig().getBoolean("Sound." + path + ".Enabled");
        String value = getInstance().getConfig().getString("Sound." + path + ".Value");
        if(value == null || !value.contains(":")){
            return;
        }
        String[] sValue_detail = value.split(":");
        if(!VersionUtil.isBelowVer1_13()){
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
