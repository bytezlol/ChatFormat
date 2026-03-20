package wtf.bytezlol;

import wtf.bytezlol.commands.FormatCommand;
import wtf.bytezlol.listener.ChatListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private String format;

    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();

        new ChatListener();
        new FormatCommand();
    }

    @Override
    public void onDisable() {}

    public void loadConfigValues() {
        reloadConfig();
        format = getConfig().getString("format");
    }
}