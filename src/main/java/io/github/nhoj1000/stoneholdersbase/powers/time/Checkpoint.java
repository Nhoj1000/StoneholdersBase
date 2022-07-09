package io.github.nhoj1000.stoneholdersbase.powers.time;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Checkpoint implements Power {
    private static final Set<UUID> IMMORTALS = new HashSet<>();

    private final int recallTime;

    public Checkpoint(int recallTime) {
        this.recallTime = recallTime;
    }

    @Override
    public boolean usePower(Player player) {
        Location loc = player.getLocation().clone();
        double health = player.getHealth();
        int food = player.getFoodLevel();
        int airTime = player.getRemainingAir();
        int fireTicks = player.getFireTicks();
        float fallDistance = player.getFallDistance();
        Collection<PotionEffect> potions = player.getActivePotionEffects();

        player.sendMessage(ChatColor.GREEN + "Checkpoint set. Returning in " + recallTime + " seconds");
        IMMORTALS.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(), () -> {
            player.teleport(loc);
            player.setHealth(health);
            player.setFoodLevel(food);
            player.setRemainingAir(airTime);
            player.setFallDistance(fallDistance);
            player.setFireTicks(fireTicks);
            player.addPotionEffects(potions);
            player.setGameMode(GameMode.SURVIVAL);
            IMMORTALS.remove(player.getUniqueId());
            Pause.setFrozen(player, false);
            player.sendMessage(ChatColor.GREEN + "Returned to checkpoint!");
        }, recallTime * 20L);
        return true;
    }

    @Override
    public ItemStack getTool() {
        return StoneUtils.generateStoneTool(Material.DIAMOND_SHOVEL, 5, "Checkpoint", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 55;
    }

    public static boolean isImmortal(Player p) {
        return IMMORTALS.contains(p.getUniqueId());
    }
}
