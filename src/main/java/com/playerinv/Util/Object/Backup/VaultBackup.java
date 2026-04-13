package com.playerinv.Util.Object.Backup;

import com.playerinv.Util.NodeUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.utils.DataFixerUtil;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class VaultBackup implements Serializable {

    private static final long serialVersionUID = 33L;

    private String uuid;

    private int num;

    private String[] strings;

    public VaultBackup(String uuid,int num,String data) {
        this.uuid = uuid;
        this.num = num;
        Inventory inventory = NodeUtil.inventoryFromBase64_Basic(data);
        ItemStack[] contents = inventory.getContents();
        this.strings = new String[contents.length];
        for (int i = 0; i < contents.length; i++) {
            if(contents[i] == null || contents[i].getType() == Material.AIR) {
                continue;
            }
            this.strings[i] = NBTEditor.getNBTCompound(contents[i]).toString();
        }
    }

    public ItemStack[] getContents(int before_version) {
        ItemStack[] contents = new ItemStack[strings.length];
        int currentVersion = DataFixerUtil.getCurrentVersion();;
        for (int i = 0; i < strings.length; i++) {
            if(strings[i] == null) {
                contents[i] = null;
                continue;
            }
            try {
                ReadWriteNBT new_nbt = DataFixerUtil.fixUpItemData(NBT.parseNBT(strings[i]), before_version, currentVersion);
                ItemStack new_item = NBT.itemStackFromNBT(new_nbt);
                contents[i] = new_item;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return contents;
    }

    public String getUuid() {
        return uuid;
    }

    public int getNum() {
        return num;
    }

    public String[] getStrings() {
        return strings;
    }
}
