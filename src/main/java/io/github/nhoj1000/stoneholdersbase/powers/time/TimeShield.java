package io.github.nhoj1000.stoneholdersbase.powers.time;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TimeShield implements Power {
    private final int slowTime, slowLevel;

    public TimeShield(int slowTime, int slowLevel) {
        this.slowTime = slowTime;
        this.slowLevel = slowLevel;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public int usePower(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slowTime * 20, slowLevel));

        return 1;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.SHIELD, 5, "Time Shield");
    }
}
