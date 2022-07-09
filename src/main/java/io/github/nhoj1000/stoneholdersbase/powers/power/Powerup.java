package io.github.nhoj1000.stoneholdersbase.powers.power;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;


public class Powerup implements Power {
    @Override
    public boolean usePower(Player player) {
        player.addPotionEffects(Arrays.asList(
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 160, 2),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 1),
                new PotionEffect(PotionEffectType.SLOW, 120, 1)
        ));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 2, 1.3f);
        return true;
    }

    @Override
    public ItemStack getTool() {
        return StoneUtils.generateStoneTool(Material.GOLDEN_SHOVEL, 1, "Powerup", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 40;
    }
}
