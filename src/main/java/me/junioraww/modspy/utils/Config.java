package me.junioraww.modspy.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Config {
  private static YamlConfiguration config;

  public static void init() {
    config = new YamlConfiguration();
    try {
      config.load("plugins/ModSpy/config.yml");
    } catch (IOException | InvalidConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public static YamlConfiguration getConfig() {
    return config;
  }

  public static void save() {
    try {
      config.save("plugins/ModSpy/config.yml");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
