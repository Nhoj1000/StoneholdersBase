package io.github.nhoj1000.stoneholdersbase.powers.soul;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;

public class PsychOut implements Power {
    private final int range;

    public PsychOut(int range) {
        this.range = range;
    }

    @Override
    public boolean usePower(Player player) {
        LivingEntity target = null;
        for(Entity e: player.getNearbyEntities(range, range, range))
            if(e instanceof LivingEntity && StoneholdersBase.isLookingAt(player, (LivingEntity) e)) {
                target = (LivingEntity) e;
                break;
            }

        if(target == null) {
            player.sendMessage("No target found!");
            return false;
        }

        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 0, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 9, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 0, false, false));
        return true;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.IRON_SHOVEL, 3, "Psych-out", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 45;
    }
}
