package dev.shadow.toomanytourneys;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TMTListeners extends PacketAdapter implements Listener {
    private final TooManyTourneys tooManyTourneys;
    private volatile String marker;

    public TMTListeners(TooManyTourneys _tooManyTourneys) {
        super(_tooManyTourneys, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT);
        tooManyTourneys = _tooManyTourneys;
        marker = loadMarker();
    }

    public void reload() {
        marker = loadMarker();
    }

    private String loadMarker() {
        return tooManyTourneys.getConfig().getString("Marker", "\u200B\u200C");
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        String marker = this.marker;
        if (marker.isEmpty()) return;

        if (packetEvent.isPlayerTemporary()) return;

        PacketContainer packet = packetEvent.getPacket();
        if (packet.getBooleans().read(0)) return;

        WrappedChatComponent chatComponent = packet.getChatComponents().read(0);
        if (chatComponent == null) return;

        Component message = GsonComponentSerializer.gson().deserialize(chatComponent.getJson());
        String plain = PlainTextComponentSerializer.plainText().serialize(message);
        if (!plain.contains(marker)) return;

        // opted out of messages
        if (tooManyTourneys.shouldHide.contains(packetEvent.getPlayer().getUniqueId())) {
            packetEvent.setCancelled(true);
            return;
        }

        // Cleans message of [TMT] Marker
        Component cleaned = message.replaceText(b -> b.matchLiteral(marker).replacement(""));

        PacketContainer copy = packet.deepClone();
        copy.getChatComponents().write(0,
                WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(cleaned)));
        packetEvent.setPacket(copy);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        tooManyTourneys.load(event.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        tooManyTourneys.shouldHide.remove(e.getPlayer().getUniqueId());
    }


}
