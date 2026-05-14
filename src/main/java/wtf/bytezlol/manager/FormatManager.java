package wtf.bytezlol.manager;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wtf.bytezlol.ChatFormat;
import wtf.bytezlol.utility.ColorUtil;
import wtf.bytezlol.utility.FileUtil;

public class FormatManager {

    private static final String FILE = "config.yml";
    private static final String DEFAULT_FORMAT = "{prefix}{name}<gray>:</gray> {message}";
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    @Getter
    private @NotNull String format = DEFAULT_FORMAT;

    public FormatManager() {
        loadConfig();
    }

    public void loadConfig() {
        final String configured = FileUtil.get(FILE).getString("format");
        this.format = configured != null && !configured.isBlank() ? configured : DEFAULT_FORMAT;
    }

    public void reload() {
        loadConfig();
    }

    public @NotNull Component render(final @NotNull Player player, final @NotNull String rawMessage) {
        final String message = processMessage(player, rawMessage);
        final String resolved = buildFormat(player).replace("{message}", message);
        return MINI.deserialize(resolved);
    }

    private @NotNull String buildFormat(final @NotNull Player player) {
        final String prefix = resolveMeta(player, true);
        final String suffix = resolveMeta(player, false);

        String result = format
                .replace("{prefix}", prefix)
                .replace("{suffix}", suffix)
                .replace("{name}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{world}", player.getWorld().getName());

        result = ColorUtil.toMiniMessage(result);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            result = PlaceholderAPI.setPlaceholders(player, result);
        }

        return result;
    }

    private @NotNull String resolveMeta(final @NotNull Player player, final boolean prefix) {
        final LuckPerms luckPerms = Bukkit.getServicesManager().load(LuckPerms.class);
        if (luckPerms == null) return "";

        final CachedMetaData meta = luckPerms.getPlayerAdapter(Player.class).getMetaData(player);
        final String raw = prefix ? meta.getPrefix() : meta.getSuffix();
        return raw == null ? "" : ColorUtil.toMiniMessage(raw);
    }

    private @NotNull String processMessage(final @NotNull Player player, final @NotNull String message) {
        final boolean colors = player.hasPermission("chatformat.colorcodes");
        final boolean rgb = player.hasPermission("chatformat.rgbcodes");

        if (colors && rgb) return ColorUtil.toMiniMessage(message);
        if (colors) return ColorUtil.stripHexCodes(ColorUtil.toMiniMessage(message));
        if (rgb) return ColorUtil.stripLegacyCodes(message);
        return ColorUtil.stripLegacyCodes(ColorUtil.stripHexCodes(message));
    }
}