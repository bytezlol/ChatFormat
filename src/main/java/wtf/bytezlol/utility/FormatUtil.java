package wtf.bytezlol.utility;

import wtf.bytezlol.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class FormatUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private FormatUtil() {}

    public static String buildFormat(final Player player) {
        final LuckPerms luckPerms = Bukkit.getServicesManager().load(LuckPerms.class);
        final CachedMetaData meta = luckPerms.getPlayerAdapter(Player.class).getMetaData(player);

        final String prefix = meta.getPrefix() != null ? ColorUtil.toMiniMessage(meta.getPrefix()) : "";
        final String suffix = meta.getSuffix() != null ? ColorUtil.toMiniMessage(meta.getSuffix()) : "";

        String format = Main.getInstance().getFormat();
        if (format == null) format = "{prefix}{name}: {message}";

        format = format
                .replace("{prefix}", prefix)
                .replace("{suffix}", suffix)
                .replace("{name}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{world}", player.getWorld().getName());

        format = ColorUtil.toMiniMessage(format);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            format = PlaceholderAPI.setPlaceholders(player, format);
        }

        return format;
    }

    public static Component render(final String format, final String processedMessage) {
        return MINI_MESSAGE.deserialize(format.replace("{message}", processedMessage));
    }

    public static String processMessage(final Player player, final String message) {
        final boolean colorCodes = player.hasPermission("chatformat.colorcodes");
        final boolean rgbCodes = player.hasPermission("chatformat.rgbcodes");

        if (colorCodes && rgbCodes) return ColorUtil.toMiniMessage(message);
        if (colorCodes) return stripHex(ColorUtil.toMiniMessage(message));
        if (rgbCodes) return stripLegacy(message);
        return stripLegacy(stripHex(message));
    }

    private static String stripLegacy(final String input) {
        return input.replaceAll("[&§][0-9a-fk-orA-FK-OR]", "");
    }

    private static String stripHex(final String input) {
        return input
                .replaceAll("[&§]#[0-9a-fA-F]{6}", "")
                .replaceAll("[&§]x([&§][0-9a-fA-F]){6}", "");
    }
}