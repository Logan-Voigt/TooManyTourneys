package dev.shadow.toomanytourneys;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ToggleCommands implements TabExecutor {
    private final TooManyTourneys plugin;

    public ToggleCommands(TooManyTourneys plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendRichMessage("<gray>Usage: /" + label + " <reload|toggle>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                if (!sender.hasPermission("toomanytourneys.reload")) return noPerm(sender);
                plugin.reload();
                sender.sendRichMessage("<green>Too Many Tourneys reloaded.");
            }
            case "toggle" -> {
                if (!(sender instanceof Player player))
                {
                    sender.sendRichMessage("<red>Only players can toggle tournament messages.");
                    return true;
                }
                if (!player.hasPermission("toomanytourneys.toggle")) return noPerm(sender);
                plugin.toggle(player);
            }
            default -> sender.sendRichMessage("<red>Unknown subcommand. Use reload, or toggle.");
        }
        return true;
    }

    private boolean noPerm(CommandSender sender) {
        sender.sendRichMessage("<red>You don't have permission.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> out = new ArrayList<>();

        if (args.length == 1) {
            for (String sub : List.of("reload", "toggle")) {
                if (sender.hasPermission("toomanytourneys." + sub) && sub.startsWith(args[0].toLowerCase())) {
                    out.add(sub);
                }
            }
        }
        return out;
    }
}