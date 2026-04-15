package me.junioraww.modspy;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin {
  private static Main plugin;
  private static Map<UUID, List<String>> installedMods = new HashMap<>();
  private static List<String> requiredModChannels = new ArrayList<>(List.of(
          "plasmo:voice/v2",
          "cpm_net:hello"
  ));

  public static Main getPlugin() {
    return plugin;
  }

  public static Map<UUID, List<String>> getInstalledMods() {
    return installedMods;
  }

  public static List<String> getRequiredModChannels() {
    return requiredModChannels;
  }

  @Override
  public void onEnable() {
    plugin = this;
    getServer().getPluginManager().registerEvents(new Events(), this);

    IngoingChannelListener listener = new IngoingChannelListener();
    getLogger().info(getServer().getMessenger().getIncomingChannels() + "");
    getServer().getMessenger().registerIncomingPluginChannel(this, "plasmo:voice/v2", listener);
    getServer().getMessenger().registerIncomingPluginChannel(this, "cpm_net:hello", listener);
  }

  @Override
  public void onDisable() {
    plugin = null;
  }
}
