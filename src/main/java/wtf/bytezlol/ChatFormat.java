package wtf.bytezlol;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.bytezlol.command.FormatCommand;
import wtf.bytezlol.listener.ChatListener;
import wtf.bytezlol.manager.FormatManager;
import wtf.bytezlol.utility.FileUtil;

@Getter
public final class ChatFormat extends JavaPlugin {

    @Getter
    private static ChatFormat instance;

    private FormatManager formatManager;

    public ChatFormat() {
        instance = this;
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        FileUtil.load("config.yml");

        this.formatManager = new FormatManager();

        new ChatListener();
        new FormatCommand();
    }
}