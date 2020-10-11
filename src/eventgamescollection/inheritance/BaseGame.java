package eventgamescollection.inheritance;

import static eventgamescollection.logging.Log.*;
import eventgamescollection.Main;
import eventgamescollection.interfaces.Loadable;

/**
 * An abstract class which every Game has to extend. Every game has a {@link #name} which is used during logging.
 * Games are handled by the {@link eventgamescollection.managers.GamesManager} which allows for loading and unloading
 * games by using commands. Loading a game will first call its constructor and then call its {@link Loadable#load()}
 * method {@link Loadable} has more information on how this should be handled.
 */
public abstract class BaseGame extends Base implements Loadable {
    private final String name;

    public BaseGame(Main plugin, String name) {
        super(plugin);
        this.name = name;
        info("Initializing " + getName());
    }

    public String getName() {
        return name;
    }
}
