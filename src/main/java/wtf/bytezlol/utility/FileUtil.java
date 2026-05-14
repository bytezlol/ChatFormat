package wtf.bytezlol.utility;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import wtf.bytezlol.ChatFormat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class FileUtil {

    private static final Map<String, FileConfiguration> LOADED = new ConcurrentHashMap<>();
    private static final Map<String, File> FILES = new ConcurrentHashMap<>();

    private FileUtil() {
        throw new AssertionError();
    }

    public static void load(final @NotNull String name) {
        final File file = new File(ChatFormat.getInstance().getDataFolder(), name);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (final InputStream stream = ChatFormat.getInstance().getResource(name)) {
                if (stream != null) ChatFormat.getInstance().saveResource(name, false);
                else file.createNewFile();
            } catch (IOException e) {
                ChatFormat.getInstance().getLogger().log(Level.SEVERE, "Failed to create file " + name, e);
                return;
            }
        }

        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        try (final InputStream defaults = ChatFormat.getInstance().getResource(name)) {
            if (defaults != null) {
                config.setDefaults(YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaults, StandardCharsets.UTF_8)));
            }
        } catch (IOException ignored) {
        }

        FILES.put(name, file);
        LOADED.put(name, config);
    }

    public static @NotNull FileConfiguration get(final @NotNull String name) {
        final FileConfiguration config = LOADED.get(name);
        if (config != null) return config;
        load(name);
        return LOADED.get(name);
    }

    public static void reload(final @NotNull String name) {
        FILES.remove(name);
        LOADED.remove(name);
        load(name);
    }

    public static void reloadAll() {
        for (final String name : LOADED.keySet().toArray(new String[0])) reload(name);
    }
}