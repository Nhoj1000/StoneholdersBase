package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class GlassBow implements Power {
    public static HashMap<UUID, Entity> targetMap = new HashMap<>();

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public int usePower(Player player) {
        Entity targetEntity = targetMap.remove(player.getUniqueId());
        if(targetEntity != null) {
            generateSphere(targetEntity.getLocation(), 4, Material.GLASS);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);

            if(targetEntity instanceof Arrow)
                targetEntity.remove();

            return 1;
        }

        return -1;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.BOW, 2, "Glass Bow");
    }

    public static void generateSphere(Location loc, int radius, Material material) {
        World world = loc.getWorld();
        int x1 = loc.getBlockX();
        int y1 = loc.getBlockY();
        int z1 = loc.getBlockZ();

        for (int x = x1 - radius; x <= x1 + radius; x++)
            for (int y = y1 - radius; y <= y1 + radius; y++)
                for (int z = z1 - radius; z <= z1 + radius; z++) {
                    int temp = (x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) + (z1 - z) * (z1 - z);
                    if (temp <= radius * radius - 1)
                        if (temp >= radius * (radius - 1.8))
                            new Location(world, x, y, z).getBlock().setType(material);
                }

    }
}
