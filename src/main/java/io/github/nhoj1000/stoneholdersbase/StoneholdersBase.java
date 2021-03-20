package io.github.nhoj1000.stoneholdersbase;

import io.github.nhoj1000.stoneholdersbase.commands.StoneSetCommand;
import io.github.nhoj1000.stoneholdersbase.events.PlayerActivatePower;
import io.github.nhoj1000.stoneholdersbase.powers.power.PowerFireball;
import io.github.nhoj1000.stoneholdersbase.powers.power.Powerup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class StoneholdersBase extends JavaPlugin {
    private final Map<Player, Stoneholder> stoneholderMap = new HashMap<>();
    private final Map<String, Stone> stones = new HashMap<>();


    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerActivatePower(this), this);

        getCommand("stone").setExecutor(new StoneSetCommand(this));

        stoneSetup();
    }

    public Map<Player, Stoneholder> getStoneholderMap() {
        return stoneholderMap;
    }

    public void stoneSetup() {
        Stone powerStone = new Stone(ChatColor.DARK_PURPLE + "Power stone");
        powerStone.registerPowers(new PowerFireball(3), new Powerup());
        registerStone("power", powerStone);
    }

    public void registerStone(String id, Stone stone) {
        stones.put(id, stone);
    }

    public Stone getStone(String id) {
        return stones.get(id);
    }
}
