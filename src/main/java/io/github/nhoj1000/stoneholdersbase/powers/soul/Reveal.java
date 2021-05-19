package io.github.nhoj1000.stoneholdersbase.powers.soul;

import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Reveal implements Power {
    private static int radius;

    public Reveal(int radius) {
        Reveal.radius = radius;
    }

    @Override
    public boolean usePower(Player player) {
        List<Entity> nearby = player.getNearbyEntities(radius, radius, radius);
        for (Entity ent : nearby)
            if (ent instanceof Player)
                ((Player) ent).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));

        player.sendMessage(ChatColor.GOLD + "All players within " + radius + " blocks revealed.");
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 10, 1.5f);
        return true;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.DIAMOND_SHOVEL, 3, "Reveal", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 30;
    }
}
