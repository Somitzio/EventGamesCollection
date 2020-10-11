package eventgamescollection.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemTools {
    /**
     * Makes the given {@link ItemStack} unbreakable.
     * @param stack The ItemStack which should be made unbreakable.
     */
    public static void makeUnbreakable(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        stack.setItemMeta(meta);
    }

    /**
     * Adds the given {@link ItemFlag}s to the item.
     * @param stack The {@link ItemStack} to which the {@link ItemFlag}s should be added to.
     * @param itemFlags The {@link ItemFlag}s which should be added.
     */
    public static void addFlags(ItemStack stack, ItemFlag... itemFlags) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(itemFlags);
        stack.setItemMeta(meta);
    }

    /**
     * Adds a name and lore to the {@link ItemStack}.
     * @param stack The {@link ItemStack} to change.
     * @param displayName A {@link String} which represents the name to give.
     * @param lore An array of {@link String}s which represent the lore.
     */
    public static void addText(ItemStack stack, String displayName, String... lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lore));
        stack.setItemMeta(meta);
    }

    /**
     * A function which returns an {@link ItemStack} with additional information and flags.
     * @param material The {@link Material} to use.
     * @param amount The amount the {@link ItemStack} should have.
     * @param unbreakable If the {@link ItemStack}
     * @param displayName
     * @param lore
     * @return The newly created {@link ItemStack}.
     */
    public static ItemStack getInformativeItemStack(Material material, int amount, boolean unbreakable, String displayName, String... lore) {
        ItemStack stack = new ItemStack(material, amount);
        if(unbreakable)
            makeUnbreakable(stack);
        addText(stack, displayName, lore);
        return stack;
    }

    /**
     * A function meant to create an {@link ItemStack} with an amount of one.
     * @param material The {@link Material} to use.
     * @param unbreakable If the the {@link ItemStack} should be unbreakable.
     * @return The newly created {@link ItemStack}.
     */
    public static ItemStack getSingleItem(Material material, boolean unbreakable, String displayName, String... lore) {
        return getInformativeItemStack(material, 1, unbreakable, displayName, lore);
    }

    /**
     * A function meant to create a single {@link org.bukkit.entity.Item} which is unbreakable and has to further information.
     * @param material The {@link Material} to use.
     * @return The newly created {@link ItemStack}.
     */
    public static ItemStack getSingleUnbreakableItem(Material material) {
        ItemStack stack = new ItemStack(material, 1);
        makeUnbreakable(stack);
        return stack;
    }
}
