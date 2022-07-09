package io.github.nhoj1000.stoneholdersbase.powers.time;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TimeShield implements UniquePower {
    private final int slowTime, slowLevel;

    public TimeShield(int slowTime, int slowLevel) {
        this.slowTime = slowTime;
        this.slowLevel = slowLevel;
    }

    @Override
    public boolean usePower(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slowTime * 20, slowLevel));
        return true;
    }

    @Override
    public Set<ItemStack> getItems() {
        return new HashSet<>(Collections.singletonList(getTimeShield()));
    }

    @Override
    public int getManaCost() {
        return 25;
    }

    @Override
    public ItemStack getActivationItem() {
        return getTimeShield();
    }

    public static ItemStack getTimeShield() {
        return StoneUtils.generateStoneTool(Material.SHIELD, 5, "Time Shield", Collections.singletonList(""));
    }
}
