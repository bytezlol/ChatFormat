package wtf.bytezlol.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import wtf.bytezlol.ChatFormat;
import wtf.bytezlol.utility.ColorUtil;
import wtf.bytezlol.utility.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FormatCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("reload");
    private static final String PREFIX = "<#FF008A><bold>FORMAT <gray>» <white>";

    public FormatCommand() {
        Objects.requireNonNull(ChatFormat.getInstance().getCommand("chatformat")).setExecutor(this);
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String[] args) {
        if (!sender.hasPermission("chatformat.admin")) {
            sender.sendMessage(ColorUtil.parse(PREFIX + "Du hast keine Berechtigung."));
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ColorUtil.parse(PREFIX + "Nutze: <#FF008A>/chatformat reload"));
            return true;
        }

        final long start = System.currentTimeMillis();
        FileUtil.reloadAll();
        ChatFormat.getInstance().getFormatManager().reload();
        final long took = System.currentTimeMillis() - start;

        sender.sendMessage(ColorUtil.parse(PREFIX + "Format reloaded <gray>(<#FF008A>" + took + "ms<gray>)."));
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String[] args) {
        if (!sender.hasPermission("chatformat.admin") || args.length != 1) return List.of();

        final String partial = args[0].toLowerCase(Locale.ROOT);
        final List<String> result = new ArrayList<>();
        for (final String sub : SUBCOMMANDS) if (sub.startsWith(partial)) result.add(sub);
        return result;
    }
}