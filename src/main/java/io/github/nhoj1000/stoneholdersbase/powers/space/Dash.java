package io.github.nhoj1000.stoneholdersbase.powers.space;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class Dash implements Power {
    private int dashDistance;

    public Dash(int dashDistance) {
        this.dashDistance = dashDistance;
    }

    @Override
    public int usePower(Player player) {
        List<Block> target = player.getLastTwoTargetBlocks(null, dashDistance);
        List<Entity> rider = player.getNearbyEntities(0.5, 1, 0.5);
        Vector velocity = player.getVelocity();
        Location destination;

        if (target.get(1).getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR) && target.get(1).getLocation().add(0, 2, 0).getBlock().getType().equals(Material.AIR))
            destination = target.get(1).getLocation().add(0, 1, 0);
        else
            destination = target.get(0).getLocation();

        destination.setDirection(player.getLocation().getDirection());
        player.sendMessage(ChatColor.BLUE + "Whoosh!");
        player.teleport(destination.add(0.5, 0, 0.5));
        player.setFallDistance(0);
        player.setVelocity(velocity);

        if (!rider.isEmpty())
            rider.get(0).teleport(player);

        return 0;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.GOLDEN_SHOVEL, 4, "Dash");
    }
}
