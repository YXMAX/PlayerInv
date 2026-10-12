package com.playerinv.manager;

import com.playerinv.object.random.RandomFloat;
import com.playerinv.object.sound.SoundEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {

    private SoundEffect menuOpen;

    private SoundEffect menuSwitch;

    private SoundEffect vaultOpen;

    private SoundEffect vaultClose;

    private SoundEffect autoPickup;

    private SoundEffect lootSend;

    private SoundEffect voucherUse;

    private SoundEffect typeChatBar;

    public SoundManager() {
        menuOpen = new SoundEffect("Menu_open",1,new RandomFloat(1), Sound.BLOCK_SHULKER_BOX_OPEN,true);
        menuSwitch = new SoundEffect("Menu_switch",1,new RandomFloat(1,2), "ITEM_BOOK_PAGE_TURN",true,"UI_BUTTON_CLICK");
        vaultOpen = new SoundEffect("Vault_open",1,new RandomFloat(1.03F), "BLOCK_ENDER_CHEST_OPEN",true,"BLOCK_ENDERCHEST_OPEN");
        vaultClose = new SoundEffect("Vault_close",1,new RandomFloat(1.03F), "BLOCK_ENDER_CHEST_CLOSE",true,"BLOCK_ENDERCHEST_CLOSE");
        autoPickup = new SoundEffect("Auto_pickup",1,new RandomFloat(1,2),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,true);
        lootSend = new SoundEffect("Loot_send",1,new RandomFloat(1,2),Sound.ENTITY_PLAYER_LEVELUP,true);
        voucherUse = new SoundEffect("Voucher_use",1,new RandomFloat(1),"UI_TOAST_CHALLENGE_COMPLETE",true,"ENTITY_ENDERDRAGON_GROWL","ENTITY_ENDER_DRAGON_GROWL");
        typeChatBar = new SoundEffect("Type_chatbar",1,new RandomFloat(1),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,true);
    }

    public void reloadSound(){
        menuOpen.reloadSound();
        menuSwitch.reloadSound();
        vaultOpen.reloadSound();
        vaultClose.reloadSound();
        autoPickup.reloadSound();
        lootSend.reloadSound();
        voucherUse.reloadSound();
        typeChatBar.reloadSound();
    }

    public void menuOpen(Player p){
        this.playSoundEffect(p,menuOpen);
    }

    public void menuSwitch(Player p){
        this.playSoundEffect(p,menuSwitch);
    }

    public void vaultOpen(Player p){
        this.playSoundEffect(p,vaultOpen);
    }

    public void vaultClose(Player p){
        this.playSoundEffect(p,vaultClose);
    }

    public void autoPickup(Player p){
        this.playSoundEffect(p,autoPickup);
    }

    public void autoPickup(Player player,int type,int remain){
        if (player == null) {
            return;
        }
        if(!autoPickup.isEnabled()){
            return;
        }
        int max = 54;
        if(type == 2){
            max = 27;
        }
        float value = (float) (2.0 - ((double) remain / max));
        player.playSound(player.getLocation(),autoPickup.getSound(),autoPickup.getVolume(),value);
    }

    public void lootSend(Player p){
        this.playSoundEffect(p,lootSend);
    }

    public void voucherUse(Player p){
        this.playSoundEffect(p,voucherUse);
    }

    public void typeChatBar(Player p){
        this.playSoundEffect(p,typeChatBar);
    }

    private void playSoundEffect(Player player, SoundEffect sound){
        if (player == null) {
            return;
        }
        if(!sound.isEnabled()){
            return;
        }
        player.playSound(player.getLocation(),sound.getSound(),sound.getVolume(),sound.getPitch().get());
    }
}
