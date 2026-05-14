package wtf.bytezlol.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import wtf.bytezlol.ChatFormat;

public final class ChatListener implements Listener {

    public ChatListener() {
        ChatFormat.getInstance().getServer().getPluginManager().registerEvents(this, ChatFormat.getInstance());
    }

    @EventHandler
    public void onChat(final @NotNull AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final String raw = LegacyComponentSerializer.legacySection().serialize(event.message());

        event.renderer((source, sourceDisplayName, msg, audience) ->
                ChatFormat.getInstance().getFormatManager().render(player, raw));
    }
}