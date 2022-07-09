package io.github.nhoj1000.stoneholdersbase.powers.space;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class Dash implements Power {
    private final int dashDistance;

    public Dash(int dashDistance) {
        this.dashDistance = dashDistance;
    }

    @Override
    public boolean usePower(Player player) {
        List<Block> target = player.getLastTwoTargetBlocks(null, dashDistance);
        List<Entity> riders = player.getNearbyEntities(0.5, 1, 0.5);
        Vector velocity = player.getVelocity().clone();

        Location destination = (target.get(1).getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR)
                             && target.get(1).getLocation().add(0, 2, 0).getBlock().getType().equals(Material.AIR))
                        ? target.get(1).getLocation().add(0.5, 1.0, 0.5)
                        : target.get(0).getLocation().subtract(0.5, 0.5, 0.5);
        destination.setDirection(player.getLocation().getDirection());
        player.teleport(StoneUtils.worldBorderBound(destination));
        player.sendMessage(ChatColor.BLUE + "Whoosh!");
        player.setFallDistance(0);
        player.setVelocity(velocity);

        riders.forEach(rider -> rider.teleport(player));
        return true;
    }

    @Override
    public ItemStack getTool() {
        return StoneUtils.generateStoneTool(Material.GOLDEN_SHOVEL, 4, "Dash", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 35;
    }
}
