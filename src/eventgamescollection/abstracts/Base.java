package eventgamescollection.abstracts;

import eventgamescollection.Main;

/**
 * An abstract class which which should be inherited by all classes which have to store {@link Main}.
 */
public abstract class Base {
    private final Main plugin;

    public Base() {
        this.plugin = Main.getPlugin();
    }

    protected Main getPlugin() {
        return plugin;
    }
}
