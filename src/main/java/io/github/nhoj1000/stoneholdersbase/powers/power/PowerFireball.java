package io.github.nhoj1000.stoneholdersbase.powers.power;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PowerFireball implements Power {
    private static int yield;

    public PowerFireball(int yield) {
        this.yield = yield;
    }

    @Override
    public int usePower(Player player) {
        Fireball fireball = (Fireball) player.getWorld().spawnEntity(player.getEyeLocation().add(player.getEyeLocation().getDirection()), EntityType.FIREBALL);
        fireball.setYield(yield);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);

        return 120;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.DIAMOND_SHOVEL, 1, "Fireball");
    }
}
