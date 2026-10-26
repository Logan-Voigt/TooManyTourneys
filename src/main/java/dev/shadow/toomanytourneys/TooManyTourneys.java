package dev.shadow.toomanytourneys;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TooManyTourneys extends JavaPlugin {
    public final Set<UUID> shouldHide = ConcurrentHashMap.newKeySet();
    private NamespacedKey key;
    private TMTListeners listeners;

    @Override
    public void onEnable()
    {

        saveDefaultConfig();
        key = new NamespacedKey(this, "hide_tourney");
        listeners = new TMTListeners(this);
        getServer().getPluginManager().registerEvents(listeners, this);

        getCommand("TooManyTourneys").setExecutor(new ToggleCommands(this));

        Bukkit.getOnlinePlayers().forEach(this::load);
        ProtocolLibrary.getProtocolManager().addPacketListener(listeners);
    }
    public void load(Player player)
    {
        if (player.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false))
            shouldHide.add(player.getUniqueId());
    }

    public void reload()
    {
        reloadConfig();
        listeners.reload();
    }

    public void toggle(Player player)
    {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        UUID playerUUID = player.getUniqueId();

        // grabs the opposite of what ever they had or the default of false.
        boolean hide = !playerPDC.getOrDefault(key, PersistentDataType.BOOLEAN, false);

        playerPDC.set(key, PersistentDataType.BOOLEAN, hide);
        if (hide) shouldHide.add(playerUUID); else shouldHide.remove(playerUUID);

        if (hide) player.sendRichMessage("<gray>Tournament messages hidden.");
        else player.sendRichMessage("<green>Tournament messages shown.");
    }


}
