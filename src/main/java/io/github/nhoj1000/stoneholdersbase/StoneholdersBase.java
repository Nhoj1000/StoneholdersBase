package io.github.nhoj1000.stoneholdersbase;

import io.github.nhoj1000.stoneholdersbase.commands.StoneSetCommand;
import io.github.nhoj1000.stoneholdersbase.events.BowShootEvent;
import io.github.nhoj1000.stoneholdersbase.events.PlayerActivatePower;
import io.github.nhoj1000.stoneholdersbase.events.EntityDamagedByEntity;
import io.github.nhoj1000.stoneholdersbase.events.PlayerDeath;
import io.github.nhoj1000.stoneholdersbase.powers.power.*;
import io.github.nhoj1000.stoneholdersbase.powers.reality.*;
import io.github.nhoj1000.stoneholdersbase.powers.soul.*;
import io.github.nhoj1000.stoneholdersbase.powers.space.*;
import io.github.nhoj1000.stoneholdersbase.powers.time.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StoneholdersBase extends JavaPlugin {
    private static final Map<UUID, Stoneholder> stoneholderMap = new HashMap<>();
    private static final Map<String, Stone> stones = new HashMap<>();

    private static StoneholdersBase plugin;


    @Override
    public void onEnable() {
        plugin = this;

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerActivatePower(), this);
        pm.registerEvents(new EntityDamagedByEntity(), this);
        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new BowShootEvent(), this);

        getCommand("stone").setExecutor(new StoneSetCommand());

        stoneSetup();
    }

    private void stoneSetup() {
        Stone powerStone = new Stone(ChatColor.DARK_PURPLE + "Power stone");
        powerStone.registerPowers(new PowerFireball(3), new Powerup(), new PowerShield(10, 1.5));
        registerStone("power", powerStone);

        Stone realityStone = new Stone(ChatColor.RED + "Reality stone");
        GlassBow gb = new GlassBow(300);
        realityStone.registerPowers(new TNTWand(30, 15), new Disguise(30, 5), gb);
        realityStone.registerPassivePowers(gb);
        registerStone("reality", realityStone);

        Stone soulStone = new Stone(ChatColor.GOLD + "Soul stone");
        soulStone.registerPowers(new Reveal(50), new AstralForm(5));
        soulStone.registerPassivePowers(new SoulCollector(20, 1, 4, 8));
        registerStone("soul", soulStone);

        Stone spaceStone = new Stone(ChatColor.BLUE + "Space stone");
        spaceStone.registerPowers(new Scatter(20, 100), new Dash(30), new Summon(12, 20));
        registerStone("space", spaceStone);

        Stone timeStone = new Stone(ChatColor.GREEN + "Time stone");
        timeStone.registerPowers(new Checkpoint(10), new Pause(20, 5), new TimeShield(10, 2));
        registerStone("time", timeStone);
    }

    public void registerStone(String id, Stone stone) {
        stones.put(id, stone);
    }

    public static Stone getStone(String id) {
        return stones.get(id);
    }

    public static Map<String, Stone> getStones() {
        return stones;
    }

    public static Map<UUID, Stoneholder> getStoneholderMap() {
        return stoneholderMap;
    }

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
}
