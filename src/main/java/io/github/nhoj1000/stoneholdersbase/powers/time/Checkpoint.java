package io.github.nhoj1000.stoneholdersbase.powers.time;

import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Checkpoint implements Power {
    private static final Set<UUID> immortals = new HashSet<>();

    private final int recallTime;
    private final StoneholdersBase plugin;

    public Checkpoint(int recallTime) {
        plugin = StoneholdersBase.getInstance();
        this.recallTime = recallTime;
    }

    @Override
    public int usePower(Player player) {
        Location loc = player.getLocation();
        double health = player.getHealth();
        int food = player.getFoodLevel();
        int airTime = player.getRemainingAir();
        Collection<PotionEffect> potions = player.getActivePotionEffects();

        player.sendMessage(ChatColor.GREEN + "Checkpoint set. Returning in " + recallTime + " seconds");
        immortals.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.teleport(loc);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFireTicks(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 255, false, false));
            player.setHealth(health);
            player.setFoodLevel(food);
            player.setRemainingAir(airTime);
            player.sendMessage(ChatColor.GREEN + "Returned to checkpoint!");
            immortals.remove(player.getUniqueId());
            Pause.setFrozen(player, false);
            for (PotionEffect effect : potions)
                    player.addPotionEffect(effect);
        }, recallTime * 20L);

        return 1;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.DIAMOND_SHOVEL, 5, "Checkpoint", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 55;
    }

    public static boolean isImmortal(Player p) {
        return immortals.contains(p.getUniqueId());
    }
}
