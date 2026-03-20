package wtf.bytezlol.commands;

import org.bukkit.command.*;
import wtf.bytezlol.Main;
import org.jetbrains.annotations.NotNull;
import wtf.bytezlol.utility.ColorUtil;

import java.util.List;
import java.util.Objects;

public final class FormatCommand implements CommandExecutor, TabCompleter {

    public FormatCommand() {
        ((PluginCommand) Objects.requireNonNull(Main.getInstance().getCommand("chatformat"))).setExecutor(this);
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final String @NotNull [] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            Main.getInstance().loadConfigValues();
            sender.sendMessage(ColorUtil.parse("&aThe config file has been reloaded."));
            return true;
        }

        sender.sendMessage(ColorUtil.parse("&cUsage: /chatformat reload"));
        return true;
    }

    @Override
    public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String alias, final String @NotNull [] args) {
        if (args.length == 1) return List.of("reload");
        return List.of();
    }
}