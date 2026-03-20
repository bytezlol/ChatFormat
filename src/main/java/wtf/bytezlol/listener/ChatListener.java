package wtf.bytezlol.listener;

import wtf.bytezlol.Main;
import wtf.bytezlol.utility.FormatUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public final class ChatListener implements Listener {

    public ChatListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncChatEvent event) {
        final Player player = event.getPlayer();

        final String format = FormatUtil.buildFormat(player);
        final String processedMessage = FormatUtil.processMessage(player,
                LegacyComponentSerializer.legacySection().serialize(event.message()));

        final Component rendered = FormatUtil.render(format, processedMessage);

        event.renderer((source, sourceDisplayName, msg, audience) -> rendered);
    }
}