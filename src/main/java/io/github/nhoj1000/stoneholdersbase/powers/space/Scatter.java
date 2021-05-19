package io.github.nhoj1000.stoneholdersbase.powers.space;

import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Scatter implements Power {
    private final int grabRange, scatterRange;
    private final Random random = new Random();

    public Scatter(int grabRange, int scatterRange) {
        this.grabRange = grabRange;
        this.scatterRange = scatterRange;
    }

    @Override
    public boolean usePower(Player player) {
        World world = player.getWorld();
        Location center = world.getWorldBorder().getCenter();
        int size = (int) world.getWorldBorder().getSize();

        List<Entity> nearby = player.getNearbyEntities(grabRange, grabRange, grabRange);

        for (Entity entity : nearby) {
            int randX = limit(player.getLocation().getBlockX() - scatterRange/2 + random.nextInt(scatterRange),
                    center.getBlockX() - size/2, center.getBlockX() + size/2);
            int randZ = limit(player.getLocation().getBlockZ() - scatterRange/2 + random.nextInt(scatterRange),
                    center.getBlockZ() - size/2, center.getBlockZ() + size/2);
            entity.teleport(new Location(world, randX, world.getHighestBlockYAt(randX, randZ) + 1, randZ));
        }
        return true;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.DIAMOND_SHOVEL, 4, "Scatter", Collections.singletonList(""));
    }

    private int limit(int i, int min, int max) {
        return Math.max(min, Math.min(i, max));
    }

    @Override
    public int getManaCost() {
        return 75;
    }
}
