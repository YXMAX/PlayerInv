package com.playerinv.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static com.playerinv.PlayerInv.operationManager;
import static com.playerinv.Util.NodeUtil.sendLog;

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
            String data = msgin.readUTF(); // Read the data in the same way you wrote it
            short somenumber = msgin.readShort();

            sendLog("data: " + data);
            if(subChannel.equals("PlayerInvCheck")) {
                operationManager.receiveCheckResult(data);
            }

        } catch (Exception e) {
            return;
        }
    }
}
