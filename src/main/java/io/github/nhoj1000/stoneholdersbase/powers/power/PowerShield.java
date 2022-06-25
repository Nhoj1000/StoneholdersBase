package io.github.nhoj1000.stoneholdersbase.powers.power;


import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class PowerShield implements UniquePower {
    private final int radius;
    private final double powerMultiplier;

    public PowerShield(int radius, double powerMultiplier) {
        this.radius = radius;
        this.powerMultiplier = powerMultiplier;
    }

    @Override
    public boolean usePower(Player player) {
        player.getNearbyEntities(radius, radius, radius).forEach(e -> {
            double distance = player.getLocation().distance(e.getLocation());
            e.setVelocity(e.getLocation().subtract(player.getLocation())
                    .toVector()
                    .normalize()
                    .multiply(powerMultiplier)
                    .multiply((radius - distance) * 0.5)
                    .add(new Vector(0, 1, 0)));
        });
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 100, 1);
        return true;
    }

    @Override
    public Set<ItemStack> getItems() {
        return Collections.singleton(getPowerShield());
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public ItemStack getActivationItem() {
        return getPowerShield();
    }

    public static ItemStack getPowerShield() {
        return Stone.generateStoneTool(Material.SHIELD, 1, "Power Shield", Collections.singletonList(""));
    }
}
