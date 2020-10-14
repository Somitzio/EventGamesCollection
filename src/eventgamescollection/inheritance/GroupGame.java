package eventgamescollection.inheritance;

import eventgamescollection.abstracts.BaseGame;
import eventgamescollection.exceptions.groupsExceptions.GroupAlreadyExistsException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A functionality class which stores lists of players.
 */
public abstract class GroupGame extends BaseGame implements Listener {
    private final HashMap<String, List<Player>> groups;
    private final HashMap<UUID, List<String>> offlinePlayers;
    private final List<List<String>> exclusiveLists;

    public GroupGame(String name) {
        super(name);
        groups = new HashMap<>();
        exclusiveLists = new ArrayList<>();
        offlinePlayers = new HashMap<>();
    }

    /**
     * Adds a group with a specific name to the hashmap.
     * @throws GroupAlreadyExistsException If another group with the same name already exists.
     * @param name The group name to add.
     */
    public void addGroup(String name) {
        if(hasGroup(name)) {
            throw new GroupAlreadyExistsException();
        } else {
            getGroups().put(name, new ArrayList<>());
        }
    }

    public void setPlayersInGroup(String name, List<Player> players) {

    }

    /**
     * Returns a list of online players in a group. To get all players in a group including offline ones use
     * {@link GroupGame#getAllPlayersInGroup(String)}.
     * @param name The group name.
     * @return A list containing all players.
     */
    public List<Player> getPlayersInGroup(String name) {
        return getGroups().get(name);
    }

    /**
     * Returns a list of {@link OfflinePlayer} which represents every player in this group. As this function needs to
     * lookup a lot it should rarely be called or be run in an asynchronous task.
     * @param name The group name.
     * @return A list of {@link OfflinePlayer}.
     */
    public List<OfflinePlayer> getAllPlayersInGroup(String name) {
        ArrayList<OfflinePlayer> players = new ArrayList<>();

        // get the offline players
        List<String> groups;
        for(UUID uuid : getOfflinePlayers().keySet()) {
            for(String group : getOfflinePlayers().get(uuid)) {
                if(group.equals(name)) {
                    players.add(getPlugin().getServer().getPlayer(uuid));
                    break;
                }
            }
        }
        return players;
    }

    public boolean hasGroup(String name) {
        return getGroups().containsKey(name);
    }

    public List<List<String>> getExclusiveLists() {
        return exclusiveLists;
    }

    public HashMap<String, List<Player>> getGroups() {
        return groups;
    }

    public HashMap<UUID, List<String>> getOfflinePlayers() {
        return offlinePlayers;
    }
}
