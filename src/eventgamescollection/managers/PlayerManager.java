package eventgamescollection.managers;

import eventgamescollection.Main;
import eventgamescollection.inheritance.BaseManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager extends BaseManager implements Listener {
    private final List<UUID> alivePlayers;
    private final List<UUID> deadPlayers;
    private Location deadPlayerSpawn;

    public PlayerManager(Main plugin) {
        super(plugin);
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.deadPlayerSpawn = null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(getPlugin().isRunning()) {
            if(!isDead(player))
                setPlayerDead(player);
            updateDeadPlayer(player);
        } else {
            setPlayerAlive(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        setPlayerDead(player);
    }

    public void updateDeadPlayer(Player player) {
        hideFromAlivePlayers(player);
        clearInventory(player);
        teleportToDeadSpawn(player);
        setAdventure(player);
    }

    public void hideFromAlivePlayers(Player player) {
        for(UUID otherUUID : getAlivePlayers()) {
            Player other = getPlugin().getServer().getPlayer(otherUUID);
            if(other != null && !player.equals(other)) {
                other.hidePlayer(player);
            }
        }
    }

    public void clearInventory(Player player) {
        player.getInventory().clear();
    }

    public void teleportToDeadSpawn(Player player) {
        player.teleport(getDeadPlayerSpawn());
    }

    public void setAdventure(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
    }

    @Override
    public void load() {
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());

        for(Player player : getPlugin().getServer().getOnlinePlayers()) {
            setPlayerAlive(player);
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public void setPlayerAlive(Player player) {
        setPlayerAlive(player.getUniqueId());
    }

    public void setPlayerAlive(UUID uuid) {
        if(!getAlivePlayers().contains(uuid))
            getAlivePlayers().add(uuid);
        getDeadPlayers().remove(uuid);
    }

    public void setPlayerDead(Player player) {
        setPlayerDead(player.getUniqueId());
    }

    public void setPlayerDead(UUID uuid) {
        if(!getDeadPlayers().contains(uuid))
            getDeadPlayers().add(uuid);
        getAlivePlayers().remove(uuid);
    }

    public boolean isAlive(Player player) {
        return isAlive(player.getUniqueId());
    }

    public boolean isAlive(UUID uuid) {
        return getAlivePlayers().contains(uuid);
    }

    public boolean isDead(Player player) {
        return isDead(player.getUniqueId());
    }

    public boolean isDead(UUID uuid) {
        return getDeadPlayers().contains(uuid);
    }

    public List<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    public List<UUID> getDeadPlayers() {
        return deadPlayers;
    }

    public Location getDeadPlayerSpawn() {
        return deadPlayerSpawn;
    }

    public void setDeadPlayerSpawn(Location deadPlayerSpawn) {
        this.deadPlayerSpawn = deadPlayerSpawn;
    }
}