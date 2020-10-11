package eventgamescollection.games.hide_and_seek;

import eventgamescollection.Main;
import eventgamescollection.exceptions.TaskAlreadyExistsException;
import eventgamescollection.games.hide_and_seek.runnables.ArrowFiller;
import eventgamescollection.inheritance.BaseGame;
import static eventgamescollection.tools.PlayerTools.*;
import static eventgamescollection.tools.ItemTools.*;
import static eventgamescollection.logging.Log.*;
import eventgamescollection.tasks.TasksHandler;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game extends BaseGame implements CommandExecutor, Listener {
    private final List<Player> hiders;
    private final List<Player> seekers;
    private final TasksHandler tasksHandler;

    public Game(Main plugin) {
        super(plugin, "Hide and Seek");
        this.hiders = new ArrayList<>();
        this.seekers = new ArrayList<>();
        this.tasksHandler = new TasksHandler(getPlugin());

        ArrowFiller arrowFiller = new ArrowFiller(getSeekers());
        getTasksHandler().addRunnable(arrowFiller);
        try {
            getTasksHandler().addTask();
        } catch (TaskAlreadyExistsException ex) {

        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length == 1) {
            String name = args[0];
            Player player = getPlugin().getServer().getPlayer(name);

            if(player != null) {
                if(label.equalsIgnoreCase("sethider")) {
                    makeHider(player);
                    commandSender.sendMessage("Successfully set " + player.getDisplayName() + " as hider.");
                } else if(label.equalsIgnoreCase("setseeker")) {
                    makeSeeker(player);
                    commandSender.sendMessage("Successfully set " + player.getDisplayName() + " as seeker.");
                }
                regen(player);
            } else {
                commandSender.sendMessage("Could not find player " + name + ".");
            }
            return true;
        }
        return false;
    }

    /**
     * @// FIXME: 26/09/2020 Give the player more information.
     * @param event
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        Entity damagerEntity = event.getDamager();

        if(!(damagedEntity instanceof Player))
            return;

        Player damager;
        if(damagerEntity instanceof Projectile) {
            Projectile projectile = (Projectile) damagerEntity;
            ProjectileSource shooter = projectile.getShooter();
            if(!(shooter instanceof Player))
                return;
            damager = (Player) shooter;
        } else if(damagerEntity instanceof Player) {
            damager = (Player) damagerEntity;
        } else {
            return;
        }

        Player damaged = (Player) event.getEntity();

        if(isSeeker(damaged) == isSeeker(damager)) {
            event.setCancelled(true);
        } else {
            double currentHP = damaged.getHealth();
            double damage = event.getFinalDamage();
            if(currentHP - damage < 0) {
                if(damaged.hasPotionEffect(PotionEffectType.SLOW)) {
                    event.setCancelled(true);
                    return;
                }
                if(isSeeker(damaged)) {
                    addPotionEffect(damaged, PotionEffectType.SLOW, 200, 5);
                    addPotionEffect(damaged, PotionEffectType.REGENERATION, 60, 5);
                    damaged.sendMessage("You were fatally wounded by " + damager.getDisplayName() + " to ");
                } else {
                    regen(damaged);
                    clear(damaged);
                    getPlugin().getPlayerManager().teleportToDeadSpawn(damaged);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof Player))
            return;
        Player player = (Player) entity;
        EntityRegainHealthEvent.RegainReason regainReason = event.getRegainReason();
        if(isHider(player) && regainReason == EntityRegainHealthEvent.RegainReason.SATIATED)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        remove(player);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity damaged = event.getEntity();
        if(damaged instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL)
            event.setCancelled(true);
    }

    private void equipHider(Player player) {
        PlayerInventory inv = player.getInventory();
        clear(player);

        ItemStack kb_stick = new ItemStack(Material.STICK, 1);
        makeUnbreakable(kb_stick);
        kb_stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        inv.setItem(0, kb_stick);

        ItemStack bow = new ItemStack(Material.BOW, 1);
        makeUnbreakable(bow);
        bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 3);
        bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        inv.setItem(1, bow);

        ItemStack wood = new ItemStack(Material.WOOD, 64);
        for(int i = 2; i <= 6; i++) {
            inv.setItem(i, wood);
        }

        ItemStack snowballs = new ItemStack(Material.SNOW_BALL, 32);
        inv.setItem(7, snowballs);

        ItemStack pearls = new ItemStack(Material.ENDER_PEARL, 5);
        inv.setItem(8, pearls);

        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        inv.addItem(arrow);
    }

    private void equipSeeker(Player player) {
        PlayerInventory inv = player.getInventory();
        clear(player);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        makeUnbreakable(sword);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        inv.setItem(0, sword);

        ItemStack wood = new ItemStack(Material.WOOD, 64);
        inv.setItem(1, wood);

        ItemStack bow = new ItemStack(Material.BOW, 1);
        makeUnbreakable(bow);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        inv.setItem(2, bow);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        makeUnbreakable(axe);
        axe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 7);
        inv.setItem(3, axe);

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        makeUnbreakable(pickaxe);
        pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 7);
        inv.setItem(4, pickaxe);

        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        makeUnbreakable(shovel);
        shovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 7);
        inv.setItem(5, shovel);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        makeUnbreakable(helmet);
        inv.setHelmet(helmet);

        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        makeUnbreakable(chestplate);
        inv.setChestplate(chestplate);

        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        makeUnbreakable(leggings);
        inv.setLeggings(leggings);

        ItemStack boots  = new ItemStack(Material.DIAMOND_BOOTS, 1);
        makeUnbreakable(boots);
        inv.setBoots(boots);

        ItemStack arrow = new ItemStack(Material.ARROW, 64);

        for(int i = 0; i <= 6; i++) {
            inv.addItem(wood);
            inv.addItem(arrow);
        }
    }

    @Override
    public void load() {
        info("Loading " + getName() + "...");
        for(UUID uuid : getPlugin().getPlayerManager().getAlivePlayers()) {
            Player player = getPlugin().getServer().getPlayer(uuid);
            regen(player);
            if(player.hasPermission("egc.games.hide_and_seek.auto_seeker"))
                makeSeeker(player);
            else
                makeHider(player);
        }

        getPlugin().getCommand("sethider").setExecutor(this);
        getPlugin().getCommand("setseeker").setExecutor(this);
        getPlugin().getPlayerManager().setDeadPlayerSpawn(getPlugin().getServer().getWorld("world").getSpawnLocation());
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
        getTasksHandler().runTask("ArrowFiller", 0, 140);
        info("Loaded " + getName());
    }

    @Override
    public void unload() {
        getPlugin().getCommand("setseeker").setExecutor(null);
        getPlugin().getCommand("sethider").setExecutor(null);
        HandlerList.unregisterAll(this);
        getTasksHandler().stopTask("ArrowFiller");
    }

    private void makeSeeker(Player player) {
        regen(player);
        setSeeker(player);
        equipSeeker(player);
    }

    private void makeHider(Player player) {
        regen(player);
        setHider(player);
        equipHider(player);
    }

    private void remove(Player player) {
        seekers.remove(player);
        hiders.remove(player);
    }

    private void setSeeker(Player player) {
        if(!seekers.contains(player))
            seekers.add(player);
        hiders.remove(player);
    }

    private void setHider(Player player) {
        if(!hiders.contains(player))
            hiders.add(player);
        seekers.remove(player);
    }

    public void clear(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[inv.getArmorContents().length]);
    }

    private boolean isHider(Player player) {
        return hiders.contains(player);
    }

    private boolean isSeeker(Player player) {
        return seekers.contains(player);
    }

    public List<Player> getHiders() {
        return hiders;
    }

    public List<Player> getSeekers() {
        return seekers;
    }

    public TasksHandler getTasksHandler() {
        return tasksHandler;
    }
}
