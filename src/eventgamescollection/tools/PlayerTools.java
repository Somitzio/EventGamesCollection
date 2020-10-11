package eventgamescollection.tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerTools {
    public static void feed(Player player) {
        player.setFoodLevel(20);
    }

    public static void heal(Player player) {
        player.setHealth(player.getMaxHealth());
    }

    public static void removeFire(Player player) {
        player.setFireTicks();
    }

    public static void regen(Player player) {
        feed(player);
        heal(player);
    }

    public static void addPotionEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        addPotionEffect(player, type, duration, amplifier, true, true);
    }

    public static void addPotionEffect(Player player, PotionEffectType type, int duration, int amplifier, boolean particles) {
        addPotionEffect(player, type, duration, amplifier, true, particles);
    }

    /**
     * Adds a given {@link PotionEffectType} with the specified specifications to the player.
     * @fixme When updating to newer versions make sure to allow for boolean icon
     * @param player The player to which the effect should be added to.
     * @param type The {@link PotionEffectType} which should be used.
     * @param duration How long the effect should last in ticks. To get your effect duration in ticks multiply your duration time by 20.
     * @param amplifier The amplifier of the effect.
     * @param ambient If ambient should be present.
     * @param particles If particles should be present.
     */
    public static void addPotionEffect(Player player, PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, ambient, particles));
    }

    public static void addHiddenPotionEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        addPotionEffect(player, type, duration, amplifier, false, false);
    }

    public static void addItem(Player player, Material material, int amount) {
        ItemStack itemStack = new ItemStack(material, amount);
    }
}
