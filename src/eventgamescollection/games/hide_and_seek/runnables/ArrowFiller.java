package eventgamescollection.games.hide_and_seek.runnables;

import static eventgamescollection.tools.InventoryTools.countItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * A runnable class which is responsible for refilling the seekers arrows every 7 seconds.
 */
public class ArrowFiller implements Runnable {
    private final List<Player> seekers;

    public ArrowFiller(List<Player> seekers) {
        this.seekers = seekers;
    }

    @Override
    public void run() {
        for(Player player : seekers) {
            Inventory inv = player.getInventory();
            int amount = countItems(inv, Material.ARROW);
            if(amount < 3) {
                ItemStack arrow = new ItemStack(Material.ARROW, 1);
                inv.addItem(arrow);
            }
        }
    }
}
