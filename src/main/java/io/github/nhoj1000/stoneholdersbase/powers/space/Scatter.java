package io.github.nhoj1000.stoneholdersbase.powers.space;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
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

        player.getNearbyEntities(grabRange, grabRange, grabRange).forEach(e -> {
            int randX = player.getLocation().getBlockX() - scatterRange/2 + random.nextInt(scatterRange);
            int randZ = player.getLocation().getBlockZ() - scatterRange/2 + random.nextInt(scatterRange);
            Location randLoc = new Location(world, randX, world.getHighestBlockYAt(randX, randZ) + 1, randZ);
            e.teleport(StoneUtils.worldBorderBound(randLoc));
        });

        return true;
    }

    @Override
    public ItemStack getTool() {
        return StoneUtils.generateStoneTool(Material.DIAMOND_SHOVEL, 4, "Scatter", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 75;
    }
}
