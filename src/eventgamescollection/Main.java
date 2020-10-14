package eventgamescollection;

import static eventgamescollection.logging.Log.*;
import eventgamescollection.abstracts.BaseGame;
import eventgamescollection.managers.GamesManager;
import eventgamescollection.managers.PlayerManager;
import eventgamescollection.managers.TestingManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main plugin;
    private boolean running;
    private final PlayerManager playerManager;
    private final GamesManager gamesManager;
    private final TestingManager testingManager;

    public Main() {
        setLogger(getLogger());
        plugin = this;
        this.running = false;
        this.playerManager = new PlayerManager();
        this.gamesManager = new GamesManager();
        // change active to true to active the TestingManager
        this.testingManager = new TestingManager(true, "/load hide and seek", "Lelleck");
    }

    @Override
    public void onEnable() {
        playerManager.load();
        gamesManager.load();
        testingManager.load();
        getLogger().info("EventGamesCollection loaded.");
    }

    @Override
    public void onDisable() {
        playerManager.unload();
        gamesManager.unload();
        testingManager.unload();
        BaseGame currentGame = getGamesManager().getCurrentGame();
        if(currentGame != null)
            currentGame.unload();
        getLogger().info("EventGamesCollection unloaded.");
    }

    public static Main getPlugin() {
        return plugin;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GamesManager getGamesManager() {
        return gamesManager;
    }
}
