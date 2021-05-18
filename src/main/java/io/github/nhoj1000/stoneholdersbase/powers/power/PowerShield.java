package io.github.nhoj1000.stoneholdersbase.powers.power;


import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PowerShield implements UniquePower {
    private final int radius;
    private final double powerMultiplier;

    public PowerShield(int radius, double powerMultiplier) {
        this.radius = radius;
        this.powerMultiplier = powerMultiplier;
    }

    @Override
    public int usePower(Player player) {
        List<Entity> entities = player.getNearbyEntities(radius, radius, radius);
        for (Entity e : entities)
            e.setVelocity(e.getLocation().subtract(player.getEyeLocation()).toVector().normalize().multiply(powerMultiplier));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 100, 1);

        return 1;
    }

    @Override
    public Set<ItemStack> getItems() {
        return new HashSet<>(Collections.singletonList(getPowerShield()));
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
