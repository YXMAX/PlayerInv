package com.playerinv.listener.channel;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.playerinv.PlayerInv;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.UUID;

import static com.playerinv.util.message.MessageUtil.sendDebug;

public class PluginChannelListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        if (!channel.equals("BungeeCord")) return;
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subChannel = in.readUTF();
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            String data = msgin.readUTF();

            if(subChannel.equals("PlayerInvCheck")) {
                sendDebug("接收到PlayerInvCheck频道的数据");
                PluginChannelListener.receiveCheckResult(data);
            }

        } catch (Exception e) {
            return;
        }
    }

    public static void sendCheckResult(int type,String uuid,int num){
        sendDebug("发送同步信息:" + uuid + ":" + num);
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteOut);

            // 指定使用 Forward
            out.writeUTF("Forward");
            // 目标服务器（例如 "lobby" 或 "ALL"）
            out.writeUTF("ALL");
            // 自定义子频道名称（你自己定义）
            out.writeUTF("PlayerInvCheck");

            // 实际要发送的数据
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream msgOut = new DataOutputStream(msgBytes);

            msgOut.writeUTF(type + "@" + uuid + "@" + num);  // 发送字符串
            msgOut.writeInt(1);      // 也可以发送更多数据

            // 写入数据长度 + 数据体
            out.writeShort(msgBytes.toByteArray().length);
            out.write(msgBytes.toByteArray());

            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if(player == null){
                sendDebug("目标服务器无玩家 未成功发送更新Check数据信号");
                return;
            }
            player.sendPluginMessage(PlayerInv.getInstance(), "BungeeCord", byteOut.toByteArray());
            sendDebug("发送更新Check数据信号至其他服务器");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void receiveCheckResult(String result){
        String[] split = result.split("@");
        int type = Integer.parseInt(split[0]);
        String uuid = split[1];
        int num = Integer.parseInt(split[2]);
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player == null){
            return;
        }
        sendDebug("执行查询修改仓库后的同步:" + result);
        PlayerInv.getInstance().getCacheInventoryManager().syncInventoryFromDatabase(player,type,num);
    }
}
