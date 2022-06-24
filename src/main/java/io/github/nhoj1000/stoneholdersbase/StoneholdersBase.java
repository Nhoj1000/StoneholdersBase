package io.github.nhoj1000.stoneholdersbase;

import io.github.nhoj1000.stoneholdersbase.commands.*;
import io.github.nhoj1000.stoneholdersbase.events.*;
import io.github.nhoj1000.stoneholdersbase.powers.power.*;
import io.github.nhoj1000.stoneholdersbase.powers.reality.*;
import io.github.nhoj1000.stoneholdersbase.powers.soul.*;
import io.github.nhoj1000.stoneholdersbase.powers.space.*;
import io.github.nhoj1000.stoneholdersbase.powers.time.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StoneholdersBase extends JavaPlugin {
    private static final Map<UUID, Stoneholder> stoneholderMap = new HashMap<>();
    private static final Map<String, Stone> stoneNameMap = new HashMap<>();
    private static final Map<ItemStack, Stone> stoneItemMap = new HashMap<>();

    private static StoneholdersBase plugin;

    @Override
    public void onEnable() {
        plugin = this;

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new StoneItemTransfer(), this);
        pm.registerEvents(new GUIHandlers(), this);
        pm.registerEvents(new PlayerActivatePower(), this);
        pm.registerEvents(new EntityDamagedByEntity(), this);
        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new BowShootEvent(), this);
        pm.registerEvents(new EntityMovingEvent(), this);
        pm.registerEvents(new PlayerScroll(), this);

        getCommand("stone").setExecutor(new StoneSetCommand());
        getCommand("noMana").setExecutor(new NoMana());

        stoneSetup();

        //Mana Regen Timer
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Stoneholder s: stoneholderMap.values())
                    s.regen();
            }
        }.runTaskTimer(this, 0L, 20L);

        Bukkit.getOnlinePlayers().forEach(StoneholdersBase::initializeStoneholder);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        stoneholderMap.values().forEach(Stoneholder::clearStones);
    }

    //region Stone helper methods
    private void stoneSetup() {
        Stone powerStone = new Stone(ChatColor.DARK_PURPLE + "Power stone", 1);
        powerStone.registerPowers(new PowerFireball(3), new Powerup());
        powerStone.registerUniquePowers(new PowerShield(10, 1.5));
        registerStone("power", powerStone);

        Stone realityStone = new Stone(ChatColor.RED + "Reality stone", 2);
        realityStone.registerPowers(new TNTWand(30, 15), new Disguise(30, 5), new GenerateArrow(5));
        realityStone.registerUniquePowers(new GlassBow(300));
        registerStone("reality", realityStone);

        Stone soulStone = new Stone(ChatColor.GOLD + "Soul stone", 3);
        soulStone.registerPowers(new Reveal(50), new AstralForm(5), new PsychOut(30));
        soulStone.registerPassivePowers(new SoulCollector(20, 1, 4, 8));
        registerStone("soul", soulStone);

        Stone spaceStone = new Stone(ChatColor.BLUE + "Space stone", 4);
        spaceStone.registerPowers(new Scatter(20, 100), new Dash(30), new Summon(12, 20));
        registerStone("space", spaceStone);

        Stone timeStone = new Stone(ChatColor.GREEN + "Time stone", 5);
        timeStone.registerPowers(new Checkpoint(10), new Pause(20, 5));
        timeStone.registerUniquePowers(new TimeShield(10, 2));
        registerStone("time", timeStone);
    }

    public void registerStone(String id, Stone stone) {
        stoneNameMap.put(id, stone);
        stoneItemMap.put(stone.getStoneItem(), stone);
    }

    public static Map<String, Stone> getStoneNameMap() {
        return stoneNameMap;
    }

    public static Stone getStoneFromItem(ItemStack i) {
        return stoneItemMap.get(i);
    }

    public static Stone getStoneFromName(String id) {
        return stoneNameMap.get(id);
    }
    //endregion

    //region Stoneholder map methods
    public static Stoneholder getStoneholder(Player p) {
        return stoneholderMap.get(p.getUniqueId());
    }

    public static boolean isStoneholder(Player p) {
        return getStoneholder(p).isStoneholder();
    }

    public static void initializeStoneholder(Player p) {
        if(!stoneholderMap.containsKey(p.getUniqueId()))
            stoneholderMap.put(p.getUniqueId(), new Stoneholder(p));
    }
    //endregion

    //region Misc helper methods
    public static ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if(meta != null)
            meta.setOwningPlayer(player);
        skull.setItemMeta(meta);
        return skull;
    }

    public static StoneholdersBase getInstance() {
        return plugin;
    }

    //Used to compare item types for power
    public static boolean comparePowerItems(ItemStack i1, ItemStack i2) {
        if(i1 == null && i2 == null) return true;
        if(i1 == null || i2 == null) return false;
        if (i1.getType() != i2.getType()) return false;

        ItemMeta m1 = i1.getItemMeta();
        ItemMeta m2 = i2.getItemMeta();
        if(m1 == null && m2 == null) return true;
        if(m1 == null || m2 == null) return false;
        if(!m1.getDisplayName().equals(m2.getDisplayName())) return false;
        if(m1.getLore() != null && m2.getLore() != null && !m1.getLore().equals(m2.getLore())) return false;
        if(m1 instanceof Damageable && m2 instanceof Damageable && ((Damageable) m1).getDamage() != ((Damageable) m2).getDamage()) return false;
        return (m1.isUnbreakable() == m2.isUnbreakable());
    }

    //Helper method to determine if one player is looking at another
    //Props to Mr.Midnight on spigotmc.org
    public static boolean isLookingAt(Player player, LivingEntity entity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = entity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }
    //endregion
}
