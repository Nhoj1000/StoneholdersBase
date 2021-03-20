package io.github.nhoj1000.stoneholdersbase;

import io.github.nhoj1000.stoneholdersbase.commands.StoneSetCommand;
import io.github.nhoj1000.stoneholdersbase.events.PlayerActivatePower;
import io.github.nhoj1000.stoneholdersbase.powers.power.*;
import io.github.nhoj1000.stoneholdersbase.powers.reality.*;
import io.github.nhoj1000.stoneholdersbase.powers.soul.*;
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
    private final Map<UUID, Stoneholder> stoneholderMap = new HashMap<>();
    private final Map<String, Stone> stones = new HashMap<>();


    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerActivatePower(this), this);

        getCommand("stone").setExecutor(new StoneSetCommand(this));

        stoneSetup();
    }

    public Map<UUID, Stoneholder> getStoneholderMap() {
        return stoneholderMap;
    }

    private void stoneSetup() {
        Stone powerStone = new Stone(ChatColor.DARK_PURPLE + "Power stone");
        powerStone.registerPowers(new PowerFireball(3), new Powerup());
        registerStone("power", powerStone);

        Stone realityStone = new Stone(ChatColor.RED + "Reality stone");
        realityStone.registerPowers(new TNTWand(30, 15), new Disguise(this, 30, 5));
        registerStone("reality", realityStone);

        Stone soulStone = new Stone(ChatColor.GOLD + "Soul stone");
        soulStone.registerPowers(new Reveal(50), new AstralForm(this, 5));
        registerStone("soul", soulStone);
    }

    public void registerStone(String id, Stone stone) {
        stones.put(id, stone);
    }

    public Stone getStone(String id) {
        return stones.get(id);
    }

    public Map<String, Stone> getStones() {
        return stones;
    }

    public static ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        skull.setItemMeta(meta);
        return skull;
    }
}
