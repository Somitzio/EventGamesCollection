package eventgamescollection.managers;

import eventgamescollection.Main;
import eventgamescollection.inheritance.BaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * A simple manager meant to allow for more ease when testing. Change it at your need.
 */
public class TestingManager extends BaseManager implements Listener {
    private final boolean active;
    private final String command;
    private final String name;

    /**
     * @param plugin The plugin to use.
     * @param active If false it will ignore all {@link BaseManager#load()} and {@link BaseManager#unload()} calls. Should be false in all releases.
     * @param command Which command to run when the player with the right name join.
     * @param name The name of the developer used to activate certain sections of code with.
     */
    public TestingManager(Main plugin, boolean active, String command, String name) {
        super(plugin);
        this.active = active;
        this.command = command;
        this.name = name;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if(event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.getName().equals(name))
            player.chat(command);
    }



    @Override
    public void load() {
        if(!active)
            return;
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @Override
    public void unload() {
        if(!active)
            return;
        HandlerList.unregisterAll(this);
    }
}
