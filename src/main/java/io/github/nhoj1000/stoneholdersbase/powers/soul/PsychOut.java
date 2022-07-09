package io.github.nhoj1000.stoneholdersbase.powers.soul;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneUtils;
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
        Player target = player.getNearbyEntities(range, range, range).stream()
                .filter(ent -> ent instanceof Player)
                .map(ent -> (Player) ent)
                .filter(p -> StoneUtils.isLookingAt(player, p))
                .findFirst().orElse(null);

        if(target == null) {
            player.sendMessage("No target found!");
            return false;
        }

        target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 10, 0, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 9, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 0, false, false));
        return true;
    }

    @Override
    public ItemStack getTool() {
        return StoneUtils.generateStoneTool(Material.IRON_SHOVEL, 3, "Psych-out", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 45;
    }
}
