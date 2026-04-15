package me.junioraww.modspy;

public class ModChannel {
  private final String name;
  private final String channel;
  private final String description;

  public ModChannel(String name, String channel, String description) {
    this.name = name;
    this.channel = channel;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getChannel() {
    return channel;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return name;
  }
}
