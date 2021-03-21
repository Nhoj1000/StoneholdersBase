package io.github.nhoj1000.stoneholdersbase.powers.time;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pause implements Power {
    private int radius, time;
    private StoneholdersBase plugin;

    public Pause(StoneholdersBase plugin, int radius, int time) {
        this.radius = radius;
        this.plugin = plugin;
    }

    @Override
    public int usePower(Player player) {
        player.sendMessage(ChatColor.GREEN + "Time Pause activated for " + time + " seconds!");
        List<Entity> ents = player.getNearbyEntities(radius, radius, radius);
        Map<Entity, Vector> speeds = new HashMap<>();

        for (Entity ent : ents) {
            if (ent instanceof Player) {
                ent.addAttachment(plugin).setPermission("stoneholders.frozen", true);
                ((Player) ent).setAllowFlight(true);
            }
            speeds.put(ent, ent.getVelocity());
            ent.setVelocity(new Vector(0, 0, 0));
            ent.setGravity(false);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Entity ent : ents) {
                ent.setGravity(true);
                if (ent instanceof Player) {
                    ent.addAttachment(plugin).setPermission("stoneholders.frozen", false);  //TODO swap gross perms out for a static set
                    ((Player) ent).setAllowFlight(((Player) ent).getGameMode() != GameMode.SURVIVAL);
                }
                ent.setVelocity(speeds.remove(ent));
            }
            player.sendMessage(ChatColor.GREEN + "Time Pause deactivated!");
        }, time * 20L);

        return 1;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.GOLDEN_SHOVEL, 5, "Pause");
    }
}
