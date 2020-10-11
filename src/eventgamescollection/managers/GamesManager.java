package eventgamescollection.managers;

import eventgamescollection.Main;
import eventgamescollection.inheritance.BaseGame;
import eventgamescollection.inheritance.BaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The Manager responsible for loading and unloading games. To create a game which can be loaded and unloaded using
 * this Manager you have to create a package inside the games package. The naming for this package should be all
 * lower case as all upper case letters will be converted to lower case when processing through
 * {@link GamesManager#getConformName(String[])}. The package must also contain a {@link BaseGame} class with the
 * name "Game" which is the class instantiated and loaded and should be considered the main class of the game.
 * The constructor of this class must only accept one parameter of type {@link Main} the name of the game should be set
 * when calling the super class {@link BaseGame#BaseGame(Main, String)}. Any extra tools specific to this package should
 * also be contained within the package.
 */
public class GamesManager extends BaseManager implements CommandExecutor {
    private BaseGame currentGame;

    public GamesManager(Main plugin) {
        super(plugin);
    }

    /**
     * Returns a name given an array of strings. This works by first parsing the given strings to all lower case
     * and then joining them with an underscore. An example would be "My Secret Game" -> "my_secret_game".  This is
     * used to find the correct package within games.
     * @param args The array of
     * @return
     */
    public String getConformName(String[] args) {
        for(String string : args)
            string = string.toLowerCase();
        return String.join("_", args);
    }

    /**
     * Uses reflection and a conform name returned by {@link GamesManager#getConformName(String[])} to find the
     * {@link BaseGame} class within the package with the given name.
     * @param name The name of the package to search for.
     * @return The instantiated {@link BaseGame}. NOTE: This instance still has to be loaded using {@link Loadable#load()}.
     * @throws NoSuchMethodException Thrown if the constructor is not found.
     * @throws ClassNotFoundException Thrown if there is no package with the given name or the {@link BaseGame} class does not exist.
     * @throws IllegalAccessException Thrown if the game instance does not have access to certain methods or fields.
     * @throws InvocationTargetException A wrapper for any exception thrown by any method.
     * @throws InstantiationException Thrown when the instantiation of the {@link BaseGame} class failed.
     */
    public BaseGame getGame(String name) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> gameClass = Class.forName("eventgamescollection.games." + name + ".Game");
        Constructor<?> gameConstructor = gameClass.getConstructor(Main.class);
        return (BaseGame) gameConstructor.newInstance(getPlugin());
    }

    /**
     * The method responsible to connect the loading logic to in game commands. It also informs the player if an
     * exception occurred.
     * Commands:
     *  - load (Required Permissions: egc.load)
     *  - unload (Required Permissions: egc.load)
     *
     * @param commandSender The {@link CommandSender} who executed the command.
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("load")) {
            if(args.length > 0) {
                String name = getConformName(args);
                try {
                    BaseGame game = getGame(name);
                    unloadCurrentGame();
                    game.load();
                    setCurrentGame(game);
                    commandSender.sendMessage("Successfully loaded game " + game.getName() + ".");
                } catch(ClassNotFoundException e) {
                    commandSender.sendMessage("Game " + name + " was not found.");
                } catch(Exception e) {
                    commandSender.sendMessage("An unknown error occurred. Please refer to the logs for more information");
                    e.printStackTrace();
                }
                return true;
            }

        } else if(label.equalsIgnoreCase("unload")) {
            if(args.length == 0) {
                BaseGame currentGame = getPlugin().getGamesManager().getCurrentGame();
                if(currentGame != null) {
                    currentGame.unload();
                    commandSender.sendMessage("Unloaded game " + currentGame.getName() + ".");
                } else {
                    commandSender.sendMessage("No game is currently loaded.");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void load() {
        getPlugin().getCommand("load").setExecutor(this);
        getPlugin().getCommand("unload").setExecutor(this);
    }

    @Override
    public void unload() {
        if(getCurrentGame() != null)
            getCurrentGame().unload();
        setCurrentGame(null);
        getPlugin().getCommand("load").setExecutor(null);
        getPlugin().getCommand("unload").setExecutor(null);
    }

    public void unloadCurrentGame() {
        if(getCurrentGame() != null) {
            getCurrentGame().unload();
            getPlugin().getGamesManager().setCurrentGame(null);
        }
    }

    public BaseGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(BaseGame currentGame) {
        this.currentGame = currentGame;
    }
}
