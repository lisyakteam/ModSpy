package me.junioraww.modspy;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Events implements Listener {
  private static MiniMessage serializer = MiniMessage.miniMessage();

  private static List<UUID> initialized = new ArrayList<>();

  static Set<ModChannel> requiredMods = Set.of(
          new ModChannel("PlasmoVoice", "plasmo:voice/v2", "Голосовой чат"),
          //new ModChannel("Emotecraft", "emotecraft", "Танцы и анимации"),
          new ModChannel("CPM", "cpm_net:hello", "Свои модели персонажей")
  );

  record TitlePair(Component first, Component second) {
    public TitlePair(String first, String second) {
      this(serializer.deserialize(first),
              serializer.deserialize(second));
    }
  }

  static Title.Times startDuration = Title.Times.times(
          Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500)
  );
  static Title.Times nextDuration = Title.Times.times(
          Duration.ZERO, Duration.ofMillis(2000), Duration.ofMillis(500)
  );
  static TitlePair firstPair = new TitlePair("", "<gradient:red:white>Не установлены моды:");
  static List<String> emptyList = new ArrayList<>();

  @EventHandler
  public void playerCheck(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    if (initialized.contains(player.getUniqueId())) return;
    initialized.add(player.getUniqueId());

    Bukkit.getAsyncScheduler().runDelayed(Main.getPlugin(), task -> {
      if (!player.isOnline()) return;
      Set<String> channels = player.getListeningPluginChannels();

      /* logging  */
      Main.getPlugin().getLogger().info(player.getName() + " channels:\n" + channels);

      List<String> respondedChannels = Main.getInstalledMods().getOrDefault(player.getUniqueId(), emptyList);

      /* checking */
      Set<ModChannel> required = new HashSet<>(requiredMods);
      required.removeIf(mod ->
              respondedChannels.stream().anyMatch(channel -> channel.equals(mod.getChannel())));

      if (!required.isEmpty()) {
        /* notifying */
        List<TitlePair> titles = new ArrayList<>(List.of(firstPair));
        required.forEach(mod -> {
          titles.add(new TitlePair("<gold><bold>" + mod.getName(), "<gray>" + mod.getDescription()));
        });
        titles.add(new TitlePair("", "<red><bold>Пожалуйста, установите!"));
        player.sendTitlePart(TitlePart.TIMES, startDuration);
        scheduleTitle(player, titles, true);
      }
    }, 1L, TimeUnit.SECONDS);
  }

  @EventHandler
  public void playerQuit(PlayerQuitEvent event) {
    initialized.remove(event.getPlayer().getUniqueId());
    Main.getInstalledMods().remove(event.getPlayer().getUniqueId());
  }

  private void scheduleTitle(Player player, List<TitlePair> titles, boolean initial) {
    if (!initial) player.sendTitlePart(TitlePart.TIMES, nextDuration);;

    if (initial) {
      sendTitle(player, titles);
    } else {
      Bukkit.getAsyncScheduler().runDelayed(Main.getPlugin(), task -> {
        sendTitle(player, titles);
      }, 2L, TimeUnit.SECONDS);
    }
  }

  private void sendTitle(Player player, List<TitlePair> titles) {
    TitlePair pair = titles.getFirst();
    Sound titleSound = Sound.sound(
            Key.key("block.note_block.pling"),
            Sound.Source.AMBIENT,
            0.7f,
            1.5f - titles.size() / 10f
    );

    player.sendTitlePart(TitlePart.TITLE, pair.first);
    player.sendTitlePart(TitlePart.SUBTITLE, pair.second);

    player.playSound(titleSound);
    if (titles.size() > 1) scheduleTitle(player, titles.subList(1, titles.size()), false);
  }

  /*@EventHandler
  public void register(PlayerRegisterChannelEvent event) {
    Player player = event.getPlayer();
    String channel = event.getChannel();
    Main.getPlugin().getLogger().info(player.getName() + " registered " + channel);
  }*/
}
