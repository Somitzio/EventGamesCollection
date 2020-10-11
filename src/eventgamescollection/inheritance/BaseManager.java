package eventgamescollection.inheritance;

import eventgamescollection.Main;
import eventgamescollection.interfaces.Loadable;

/**
 * An abstract class which every Manager should inherit. Managers are systems which manage games or provide features
 * which should be available during the entire runtime of the plugin. Managers must be hard-coded into {@link Main}
 * to be loaded. Provided Managers are {@link eventgamescollection.managers.GamesManager} which loads and unloads games
 * and {@link eventgamescollection.managers.PlayerManager} which stores and manages dead and alive players.
 */
public abstract class BaseManager extends Base implements Loadable {
    public BaseManager(Main plugin) {
        super(plugin);
    }
}
