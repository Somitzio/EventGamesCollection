package eventgamescollection.tools;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryTools {
    /**
     * Counts the amount of occurrences of a {@link Material} in an {@link Inventory} this includes the number of items
     * in an {@link ItemStack}. Armor slots are ignored.
     * @param inv The inventory to count in.
     * @param material The material to search for.
     * @return The amount of items in the inventory.
     */
    public static int countItems(Inventory inv, Material material) {
        ItemStack[] items = inv.getContents();
        int amount = 0;
        for(ItemStack stack : items) {
            if(stack != null && stack.getType() == material) {
                amount += stack.getAmount();
            }
        }
        return amount;
    }
}
