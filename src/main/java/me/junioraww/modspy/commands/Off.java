package me.junioraww.modspy.commands;

import me.junioraww.modspy.utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Off implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      Config.getConfig().set("disabled-spy." + player.getName().toLowerCase(), true);
      player.sendRichMessage("<#33FF66>Предупреждения о неустановленных модах отключены!");
    }

    return true;
  }
}
