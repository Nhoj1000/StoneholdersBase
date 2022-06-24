package io.github.nhoj1000.stoneholdersbase.powers.power;

import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;


public class PowerFireball implements Power {
    private final int yield;

    public PowerFireball(int yield) {
        this.yield = yield;
    }


    @Override
    public boolean usePower(Player player) {
        Fireball fireball = (Fireball) player.getWorld().spawnEntity(player.getEyeLocation().add(player.getEyeLocation().getDirection()), EntityType.FIREBALL);
        fireball.setYield(yield);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
        return true;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.DIAMOND_SHOVEL, 1, "Fireball", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 30;
    }
}
