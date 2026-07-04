package me.junioraww.modspy.listeners;

import me.junioraww.modspy.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngoingChannelListener implements PluginMessageListener {
  @Override
  public void onPluginMessageReceived(@NotNull String channel, Player player, byte[] message) {
    if (Main.getRequiredModChannels().contains(channel)) {
      UUID uuid = player.getUniqueId();
      var players = Main.getInstalledMods();

      if (!players.containsKey(uuid)) players.put(uuid, new ArrayList<>());
      List<String> mods = players.get(uuid);
      if (!mods.contains(channel)) mods.add(channel);
    }
  }
}
