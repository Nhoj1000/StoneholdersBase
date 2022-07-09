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

import java.util.*;

import static io.github.nhoj1000.stoneholdersbase.StoneConstants.*;

public final class StoneholdersBase extends JavaPlugin {
    private static final Map<UUID, Stoneholder> STONEHOLDER_MAP = new HashMap<>();

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
                STONEHOLDER_MAP.values().forEach(Stoneholder::regen);
            }
        }.runTaskTimer(this, 0L, 20L);

        Bukkit.getOnlinePlayers().forEach(StoneholdersBase::initializeStoneholder);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        STONEHOLDER_MAP.values().forEach(Stoneholder::clearStones);
    }

    public static StoneholdersBase getInstance() {
        return plugin;
    }

    public static Stoneholder getStoneholder(Player p) {
        return STONEHOLDER_MAP.get(p.getUniqueId());
    }

    public static void initializeStoneholder(Player p) {
        Stoneholder stoneholder = getStoneholder(p);
        if(stoneholder == null) {
            STONEHOLDER_MAP.put(p.getUniqueId(), new Stoneholder(p));
        } else {
            stoneholder.setPlayer(p);
            stoneholder.updateMaxMana();
        }
    }

    private void stoneSetup() {
        StoneUtils.registerStone(new Stone(POWER_ID, ChatColor.DARK_PURPLE, 1)
                .registerPowers(
                        new PowerFireball(3),
                        new Powerup())
                .registerUniquePowers(
                        new PowerShield(10, 1.5)));

        StoneUtils.registerStone(new Stone(REALITY_ID, ChatColor.RED, 2)
                .registerPowers(
                        new TNTWand(30, 15),
                        new Disguise(30, 5),
                        new GenerateArrow(5))
                .registerUniquePowers(
                        new GlassBow(300)));

        StoneUtils.registerStone(new Stone(SOUL_ID, ChatColor.GOLD, 3)
                .registerPowers(
                        new Reveal(50),
                        new AstralForm(5),
                        new PsychOut(30))
                .registerPassivePowers(
                        new SoulCollector(20, 1, 4, 8)));

        StoneUtils.registerStone(new Stone(SPACE_ID, ChatColor.BLUE, 4)
                .registerPowers(
                        new Scatter(20, 100),
                        new Dash(30),
                        new Summon(12, 20)));

        StoneUtils.registerStone(new Stone(TIME_ID, ChatColor.GREEN, 5)
                .registerPowers(
                        new Checkpoint(10),
                        new Pause(20, 5))
                .registerUniquePowers(
                        new TimeShield(10, 2)));
    }
}
