package com.playerinv.GUI;


import com.playerinv.Util.LoadUtil;
import com.playerinv.Util.NodeUtil;

public class MenuItemException {

    public void invalidMaterial(String file, String path, String k, boolean replace){
        if(k == null){
            k = "null";
        }
        String replacing = " , Disable this items...";
        if(LoadUtil.locale.equals("zh-CN")){
            replacing = " , 禁用该自定义展示物品..";
        }
        if(replace){
            replacing = " , replace to the material CHEST";
            if(LoadUtil.locale.equals("zh-CN")){
                replacing = " , 替换物品材质为: 箱子";
            }
        }
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] " + file + ":" + path + " 包含一个未知的物品材质:" + k + replacing);
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] " + file + ":" + path + " contains an invalid item material:" + k + replacing);
    }

    public int checkVaultSize(String menu,int size){
        switch(size){
            case 9:
            case 18:
            case 27:
            case 36:
            case 45:
            case 54:
                return size;
            default:
                if(LoadUtil.locale.equals("zh-CN")){
                    NodeUtil.sendConsole("&c[菜单警告] " + menu + " 菜单大小错误! 修改菜单大小为 54...");
                    return 54;
                }
                NodeUtil.sendConsole("&c[MENU_WARN] " + menu + " invalid size! Change the menu size to 54...");
                return 54;
        }
    }

    public void invalidVaultAmount(String path){
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] " + path + " 未知仓库编号! 请确保Config.yml内设置的仓库数量大于该编号! 该物品将被禁用!");
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] " + path + " is an invalid vault id! please extend the amount of vault in config.yml! Disabled this items...");
    }

    public void invalidSlot(String path){
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] " + path + " 路径下的自定义展示物品没有标明槽位 (slot)! 该物品将被禁用!");
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] " + path + "'s slot is not exist! Disabled this items...");
    }

    public void oversizeSlot(String path){
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] " + path + " 路径下的自定义展示物品槽位 (slot) 大于菜单大小! 该物品将被禁用!");
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] " + path + "'s slot is over menu size! Disabled this items...");
    }

    public void invalidAmount(String path){
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] " + path + " 路径下的自定义展示物品数量 (amount) 不可用! 该物品将被禁用!");
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] " + path + "'s amount is invalid! Set to default: 1");
    }

    public void unknownVaultNum(String path){
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] " + path + " 格式错误 非法的仓库编号! 该物品将被禁用!");
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] " + path + " detected a unknown vault number! Disabled this items...");
    }

    public void oversizeBar(String path){
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] " + path + " 路径下的自定义展示物品槽位 (slot) 超出允许的搜索导航栏槽位 (46-54)! 该物品将被禁用!");
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] " + path + "'s slot is over allowed-size! Disabled this items...");
    }

    public void invalidTitle(String path){
        if(LoadUtil.locale.equals("zh-CN")){
            NodeUtil.sendConsole("&c[菜单警告] Confirm_menu.yml 下缺少菜单名称: " + path);
            return;
        }
        NodeUtil.sendConsole("&c[MENU_WARN] Confirem_menu.yml lack a menu title: "  + path);
    }
}
