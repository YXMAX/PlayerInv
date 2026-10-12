package com.playerinv.service.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.playerinv.PlayerInv;
import com.playerinv.util.exception.ItemNullDenyException;
import com.playerinv.util.exception.ItemNullSkipException;
import com.playerinv.gui.holder.impl.vault.CheckVaultHolder;
import com.playerinv.util.NodeUtil;
import com.playerinv.object.container.InventoryContainer;
import de.tr7zw.changeme.nbtapi.*;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static com.playerinv.enums.LocaleEnums.*;
import static com.playerinv.util.LocaleUtil.sendMessages;
import static com.playerinv.util.NodeUtil.sendSerializerError;
import static com.playerinv.util.message.MessageUtil.sendDebug;
import static com.playerinv.util.NodeUtil.sendConsole;

public class InventorySerializer {

    // -1:禁止 0:移除 1:修复
    private static int itemPolicy;

    private static final LZ4Factory lz4Factory = LZ4Factory.fastestJavaInstance();

    public static void loadPolicy(){
        String string = PlayerInv.getInstance().getConfig().getString("Invalid_item_policy", "deny");
        if(string.equalsIgnoreCase("remove")){
            sendConsole("&6仓库反序列化失败策略: &a修复物品");
            itemPolicy = 1;
        } else if(string.equalsIgnoreCase("skip")){
            sendConsole("&6仓库反序列化失败策略: &e跳过物品");
            itemPolicy = 0;
        } else {
            sendConsole("&6仓库反序列化失败策略: &c禁止打开");
            itemPolicy = -1;
        }
    }

    public static boolean isDenyPolicy(){
        return itemPolicy == -1;
    }

    private static void sendSerializeError(int type,String uuid,int num,int slot){
        switch(type){
            case 1:
                NodeUtil.sendSerializerError("[LARGE-VAULT] Deserialize item failed: " + uuid + ":" + num + "//" + slot);
                return;
            case 2:
                NodeUtil.sendSerializerError("[MEDIUM-VAULT] Deserialize item failed: " + uuid + ":" + num + "//" + slot);
                return;
            case 3:
                NodeUtil.sendSerializerError("[SHARE-VAULT] Deserialize item failed: " + uuid + ":" + num + "//" + slot);
                return;
        }
    }

    private static void saveErrorFile(JsonArray array, int type, String uuid, int num){
        String vault_type = "large";
        if(type == 2){
            vault_type = "medium";
        }
        Path path = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(),"error_vaults", vault_type, uuid + "_" + num + ".json").normalize();
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, PlayerInv.getInstance().getGsonParser().toPrettyJson(array).getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
            sendSerializerError("已将出现错误的仓库JSON数据保存至插件配置文件目录内: error_vaults/" + vault_type + "/" + uuid + "_" + num + ".json");
            sendSerializerError("你可对该JSON数据进行手动修复 并可将该数据复制到数据库中让插件重新读取");
        } catch (FileAlreadyExistsException e2){
            sendSerializerError("该仓库JSON数据文件已存在 文件写入失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveSkipFile(JsonArray array,int type,String uuid,int num){
        String vault_type = "large";
        if(type == 2){
            vault_type = "medium";
        }
        Path path = Paths.get(PlayerInv.getInstance().getDataFolder().getAbsolutePath(),"skip_vaults", vault_type, uuid + "_" + num + ".json").normalize();
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, PlayerInv.getInstance().getGsonParser().toPrettyJson(array).getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
            sendSerializerError("已将出现物品解析错误并跳过的仓库JSON数据保存至插件配置文件目录内: skip_vaults/" + vault_type + "/" + uuid + "_" + num + ".json");
            sendSerializerError("该仓库策略下无法直接把修复后的JSON数据放入数据库中重新读取 只允许在关闭服务器后再对数据库进行更新");
        } catch (FileAlreadyExistsException e2){
            sendSerializerError("该物品解析错误并跳过的仓库JSON数据文件已存在 文件写入失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEmptyInventoryString(int type){
        ItemStack[] itemStacks = new ItemStack[getStackSize(type)];
        return serializeInventory(itemStacks);
    }

    public static String serializeInventory(ItemStack[] itemStacks){
        return "JL:" + base64Encode(lz4Compress(jsonCompress(itemStacks)));
    }

    public static InventoryContainer deserializeVaultInventory(Player watch_player, int type, String uuid, int num, String data){
        return deserializeInventory(watch_player,type,uuid,num,data,-1,false);
    }

    public static CheckVaultHolder deserializeCheckInventory(Player watch_player, int type, int num, OfflinePlayer target, Inventory back_inventory, String data){
        if(data == null) return null;
        sendDebug("尝试解析仓库数据: " + type + "//" + target.getUniqueId() + ":" + num);
        byte[] result;
        boolean isJson = false;
        ItemStack[] itemStacks = null;
        if(data.startsWith("JL:")){
            sendDebug("确认为新JSON仓库数据格式: " + type + "//" + target.getUniqueId() + ":" + num);
            isJson = true;
            data = data.substring(3);
        }

        // 第一步：尝试 Base64 解码
        try {
            result = base64Decode(data);
        } catch (IllegalArgumentException e) {
            // 非 Base64 数据
            return null;
        }

        byte[] lz4_decoded = lz4Decompress(result);

        if(lz4_decoded != null){
            // 成功解压 说明是JSON或新的bukkit数据流
            if(isJson){
                sendDebug("进入JSON数据分支: " + type + "//" + target.getUniqueId() + ":" + num);
                // 新的JSON数据格式
                try {
                    itemStacks = jsonDecompress(new String(lz4_decoded,"UTF-8"),type,target.getUniqueId().toString(),num,watch_player);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sendDebug("进入Bukkit数据流v2分支: " + type + "//" + target.getUniqueId() + ":" + num);
                // Bukkit数据流
                itemStacks = bukkitDecompress(lz4_decoded,type,target.getUniqueId().toString(),num,watch_player);
            }
        } else {
            sendDebug("进入Bukkit数据流v1分支: " + type + "//" + target.getUniqueId() + ":" + num);
            itemStacks = bukkitDecompress(result,type,target.getUniqueId().toString(),num,watch_player);
        }
        if(itemStacks == null) return null;
        InventoryContainer container = new InventoryContainer(type);
        return container.setCheckInventoryHolder(itemStacks,type,num,target,back_inventory);
    }

    public static InventoryContainer deserializeInventory(Player watch_player, int type, String uuid, int num, String data, int permission, boolean checking){
        if(data == null) return null;
        sendDebug("尝试解析仓库数据: " + type + "//" + uuid + ":" + num);
        byte[] result;
        boolean isJson = false;
        ItemStack[] itemStacks = null;
        if(data.startsWith("JL:")){
            sendDebug("确认为新JSON仓库数据格式: " + type + "//" + uuid + ":" + num);
            isJson = true;
            data = data.substring(3);
        }
        if(data.startsWith("[") && data.endsWith("]")){
            sendDebug("确认为JSON原始数据格式: " + type + "//" + uuid + ":" + num);
            sendDebug("进入JSON数据分支: " + type + "//" + uuid + ":" + num);
            itemStacks = jsonDecompress(data,type,uuid,num,watch_player);
            if(itemStacks == null) return new InventoryContainer(type,null,true);
            InventoryContainer container = new InventoryContainer(type);
            container.setInventory(itemStacks,uuid,num,permission,checking,watch_player);
            return container;
        }

        // 第一步：尝试 Base64 解码
        try {
            result = base64Decode(data);
        } catch (IllegalArgumentException e) {
            // 非 Base64 数据
            return null;
        }

        byte[] lz4_decoded = lz4Decompress(result);

        if(lz4_decoded != null){
            // 成功解压 说明是JSON或新的bukkit数据流
            if(isJson){
                sendDebug("进入JSON数据分支: " + type + "//" + uuid + ":" + num);
                // 新的JSON数据格式
                try {
                    itemStacks = jsonDecompress(new String(lz4_decoded,"UTF-8"),type,uuid,num,watch_player);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sendDebug("进入Bukkit数据流v2分支: " + type + "//" + uuid + ":" + num);
                // Bukkit数据流
                itemStacks = bukkitDecompress(lz4_decoded,type,uuid,num,watch_player);
            }
        } else {
            sendDebug("进入Bukkit数据流v1分支: " + type + "//" + uuid + ":" + num);
            itemStacks = bukkitDecompress(result,type,uuid,num,watch_player);
        }
        if(itemStacks == null) return new InventoryContainer(type,null,true);
        InventoryContainer container = new InventoryContainer(type);
        container.setInventory(itemStacks,uuid,num,permission,checking,watch_player);
        return container;
    }

    private static String itemSerializer(ItemStack itemStack){
        return NBTItem.convertItemtoNBT(itemStack).toString();
    }

    private static ItemStack itemDeserializer(String s){
        switch(itemPolicy){
            case 1:
                return deserializeWithRemove(s);
            case 0:
                return deserializeWithSkip(s);
            case -1:
            default:
                return deserializeWithDeny(s);
        }
    }

    private static ItemStack deserializeWithRemove(String s){
        try {
            NBTContainer nbt = new NBTContainer(s);
            removeInvalidEnchantments(nbt);
            return NBTItem.convertNBTtoItem(nbt);
        } catch (Exception e){
            throw new ItemNullSkipException();
        }
    }

    private static ItemStack deserializeWithSkip(String s){
        try {
            ItemStack itemStack = NBTItem.convertNBTtoItem((NBTCompound) new NBTContainer(s));
            if(itemStack == null){
                throw new ItemNullSkipException();
            }
            return itemStack;
        } catch (Exception e){
            throw new ItemNullSkipException();
        }
    }

    private static ItemStack deserializeWithDeny(String s){
        try {
            ItemStack itemStack = NBTItem.convertNBTtoItem((NBTCompound) new NBTContainer(s));
            if(itemStack == null){
                throw new ItemNullDenyException();
            }
            return itemStack;
        } catch (Exception e){
            throw new ItemNullDenyException();
        }
    }

    private static String base64Encode(byte[] stream){
        return Base64.getEncoder().encodeToString(stream);
    }

    private static byte[] base64Decode(String data){
        return Base64.getMimeDecoder().decode(data);
    }

    private static byte[] lz4Compress(byte[] stream){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LZ4Compressor compressor = lz4Factory.fastCompressor();
            LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(out,2048,compressor);
            compressedOutput.write(stream);
            compressedOutput.close();
            return out.toByteArray();
        } catch (Exception e){
            return null;
        }
    }

    private static byte[] lz4Decompress(byte[] decoded_base64){
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded_base64);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            LZ4BlockInputStream lz4Input = new LZ4BlockInputStream(inputStream, lz4Factory.fastDecompressor());
            byte[] buffer = new byte[1024];
            int count;
            while ((count = lz4Input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } catch (Exception e){
            return null;
        }
    }

    private static byte[] bukkitCompress(ItemStack[] itemStacks){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(itemStacks.length);
            for (ItemStack itemStack : itemStacks) {
                dataOutput.writeObject(itemStack);
            }
            dataOutput.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    private static ItemStack[] bukkitDecompress(byte[] decoded,int type,String uuid,int num,Player player){
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] itemStacks = new ItemStack[dataInput.readInt()];
            boolean notice = false;
            for (int i = 0; i < itemStacks.length; i++) {
                ItemStack item = null;
                try {
                    item = (ItemStack) dataInput.readObject();
                } catch (Exception e){
                    if(isDenyPolicy()){
                        if(player != null){
                            sendMessages(player,Vault_item_broken_deny,"%vault_type%",String.valueOf(type),"%vault_num%",String.valueOf(num));
                        }
                        sendSerializeError(type,uuid,num,i+1);
                        return null;
                    }
                    if(player != null && !notice){
                        sendMessages(player,Vault_item_broken_remove,"%vault_type%",String.valueOf(type),"%vault_num%",String.valueOf(num));
                        notice = true;
                    }
                    sendSerializeError(type,uuid,num,i+1);
                    continue;
                }
                if(item == null || item.getType() == Material.AIR){
                    continue;
                }
                itemStacks[i] = item;
            }
            dataInput.close();
            return itemStacks;
        } catch(Exception e) {
            return null;
        }
    }

    private static byte[] jsonCompress(ItemStack[] itemStacks){
        JsonArray json = new JsonArray();
        for(int i=0;i<itemStacks.length;i++){
            ItemStack item = itemStacks[i];
            if(item != null && item.getType() != Material.AIR){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("slot", i);
                jsonObject.addProperty("item", itemSerializer(item));
                json.add(jsonObject);
            }
        }
        try {
            return json.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static ItemStack[] jsonDecompress(String decode,int type,String uuid,int num,Player player){
        ItemStack[] itemStacks = new ItemStack[getStackSize(type)];
        JsonArray array = PlayerInv.getInstance().getGsonParser().parseToJsonArray(decode);
        boolean notice = false;
        boolean deny = false;
        boolean skip = false;
        for(JsonElement element : array){
            int slot = 0;
            try {
                ItemStack item;
                JsonObject jsonObject = element.getAsJsonObject();
                slot = jsonObject.get("slot").getAsInt();
                String is = jsonObject.get("item").getAsString();
                item = itemDeserializer(is);
                if(item == null) continue;
                itemStacks[slot] = item;
            } catch (IllegalStateException e){
                continue;
            } catch (ItemNullSkipException e1){
                skip = true;
                if(player != null && !notice){
                    sendMessages(player,Vault_item_broken_remove,"%vault_type%",String.valueOf(type),"%vault_num%",String.valueOf(num));
                    notice = true;
                }
                sendSerializeError(type,uuid,num,slot+1);
                continue;
            } catch (ItemNullDenyException e2){
                deny = true;
                if(player != null && !notice){
                    sendMessages(player,Vault_item_broken_deny,"%vault_type%",String.valueOf(type),"%vault_num%",String.valueOf(num));
                    notice = true;
                }
                sendSerializeError(type,uuid,num,slot+1);
                continue;
            }
        }
        if(deny){
            saveErrorFile(array,type,uuid,num);
            return null;
        }
        if(skip){
            saveSkipFile(array,type,uuid,num);
        }
        return itemStacks;
    }

    private static int getStackSize(int vaultType){
        switch(vaultType){
            case 2:
                return 27;
            case 3:
                return 36;
            case 1:
            default:
                return 54;
        }
    }

    private static boolean isInvalidEnchantment(String enchantmentKey) {
        try {
            NamespacedKey key;
            if(enchantmentKey.contains(":")){
                String[] split = enchantmentKey.split(":");
                key = new NamespacedKey(split[0],split[1]);
            } else {
                key = NamespacedKey.minecraft(enchantmentKey);
            }
            return Enchantment.getByKey(key) == null;
        } catch (Throwable e) {
            return true;
        }
    }

    private static void removeInvalidEnchantments(NBTContainer nbt) {
        if (nbt.hasTag("components")) {
            NBTCompound components = nbt.getCompound("components");
            if (components == null) return;
            cleanEnchantmentCompound(components, "minecraft:enchantments");
            cleanEnchantmentCompound(components, "minecraft:stored_enchantments");
        }
        if (nbt.hasTag("tag")) {
            NBTCompound tag = nbt.getCompound("tag");
            if (tag == null) return;
            cleanEnchantmentList(tag, "Enchantments");
            cleanEnchantmentList(tag, "StoredEnchantments");
        }
    }

    private static void cleanEnchantmentCompound(NBTCompound components, String key){
        if(!components.hasTag(key)) return;
        NBTCompound enchCompound = components.getCompound(key);
        if (enchCompound == null) return;
        NBTCompound levels = enchCompound.hasTag("levels") ? enchCompound.getCompound("levels") : enchCompound;
        if(levels == null) return;
        Set<String> keys = new HashSet<>(levels.getKeys());
        if(keys.isEmpty()) return;
        for(String s : keys){
            if(isInvalidEnchantment(s)){
                sendDebug("检测到无效附魔 进行移除: " + s);
                levels.removeKey(s);
            }
        }
    }

    private static void cleanEnchantmentList(NBTCompound tag, String key) {
        if (!tag.hasTag(key)) return;
        NBTCompoundList enchList = tag.getCompoundList(key);
        if (enchList == null) return;
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < enchList.size(); i++) {
            NBTListCompound entry = enchList.get(i);
            String id = entry.getString("id");
            if(id == null) continue;
            if (isInvalidEnchantment(id)) {
                sendDebug("检测到无效附魔 进行移除: " + id);
                ids.add(i);
            }
        }
        for(int i : ids){
            enchList.remove(i);
        }
    }
}
